package com.example.kotlinshowcase.feature.amiibo.data.remote

import com.example.kotlinshowcase.feature.amiibo.domain.model.Amiibo
import com.example.kotlinshowcase.feature.amiibo.domain.model.AmiiboResponse
import com.example.kotlinshowcase.feature.amiibo.domain.model.ReleaseDate
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AmiiboServiceImplTest {

    private val baseUrl = "https://www.amiiboapi.com"
    private val json = Json { 
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = true
    }
    
    private val testAmiibo = Amiibo(
        name = "Mario",
        character = "Mario",
        gameSeries = "Super Mario",
        image = "mario.png",
        amiiboSeries = "Super Smash Bros.",
        type = "Figure",
        release = ReleaseDate("2014-11-21")
    )
    
    private val testAmiibo2 = Amiibo(
        name = "Link",
        character = "Link",
        gameSeries = "The Legend of Zelda",
        image = "link.png",
        amiiboSeries = "The Legend of Zelda",
        type = "Figure",
        release = ReleaseDate("2017-03-03")
    )
    
    private fun createMockEngine(
        statusCode: HttpStatusCode = HttpStatusCode.OK,
        response: String = ""
    ) = MockEngine { request ->
        respond(
            content = response,
            status = statusCode,
            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        )
    }
    
    private fun createHttpClient(mockEngine: MockEngine) = io.ktor.client.HttpClient(mockEngine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
    }
    
    @Test
    fun `getAmiibos returns list of amiibos on success`() = runTest {
        val expectedResponse = AmiiboResponse(amiibo = listOf(testAmiibo, testAmiibo2))
        val mockEngine = createMockEngine(
            response = json.encodeToString(expectedResponse)
        )
        val service = AmiiboServiceImpl(createHttpClient(mockEngine))

        val result = service.getAmiibos()

        assertEquals(2, result.size)
        assertEquals("Mario", result[0].name)
        assertEquals("Link", result[1].name)
    }
    
    @Test
    fun `searchAmiibos with non-empty query returns filtered list`() = runTest {
        val query = "mario"
        val expectedResponse = AmiiboResponse(amiibo = listOf(testAmiibo))
        val mockEngine = createMockEngine(
            response = json.encodeToString(expectedResponse)
        )
        val service = AmiiboServiceImpl(createHttpClient(mockEngine))

        val result = service.searchAmiibos(query)

        assertEquals(1, result.size)
        assertEquals("Mario", result[0].name)
    }
    
    @Test
    fun `searchAmiibos with empty query returns empty list`() = runTest {
        val service = AmiiboServiceImpl(createHttpClient(createMockEngine()))

        val result = service.searchAmiibos("")

        assertTrue(result.isEmpty())
    }
    
    @Test(expected = Exception::class)
    fun `getAmiibos throws exception on network error`() = runTest {
        val mockEngine = MockEngine { respondError(HttpStatusCode.InternalServerError) }
        val service = AmiiboServiceImpl(createHttpClient(mockEngine))

        service.getAmiibos()
    }
    
    @Test
    fun `searchAmiibos converts query to lowercase`() = runTest {
        val query = "MARIO"
        val expectedResponse = AmiiboResponse(amiibo = listOf(testAmiibo))
        var requestUrl: String? = null
        
        val mockEngine = MockEngine { request ->
            requestUrl = request.url.toString()
            respond(
                content = json.encodeToString(expectedResponse),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        
        val service = AmiiboServiceImpl(createHttpClient(mockEngine))

        service.searchAmiibos(query)

        assertTrue(requestUrl?.contains("name=mario") == true)
    }
}
