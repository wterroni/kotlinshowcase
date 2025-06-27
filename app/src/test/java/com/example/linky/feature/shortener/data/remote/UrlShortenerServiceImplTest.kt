package com.example.linky.feature.shortener.data.remote

import com.example.linky.feature.shortener.domain.model.ShortenUrlRequest
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UrlShortenerServiceImplTest {
    
    private lateinit var mockEngine: MockEngine
    private lateinit var httpClient: HttpClient
    private lateinit var service: UrlShortenerServiceImpl
    
    private val baseUrl = "https://url-shortener-nu.herokuapp.com"
    
    @Before
    fun setup() {
        mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/api/alias" -> {
                    respond(
                        content = """
                            {
                                "alias": "abc123",
                                "_links": {
                                    "self": "https://example.com",
                                    "short": "https://sou.nu/abc123"
                                }
                            }
                        """.trimIndent(),
                        status = HttpStatusCode.Created,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                "/api/alias/abc123" -> {
                    respond(
                        content = """
                            {
                                "url": "https://example.com"
                            }
                        """.trimIndent(),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                "/api/alias/notfound" -> {
                    respond(
                        content = "Not Found",
                        status = HttpStatusCode.NotFound,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                else -> {
                    respond(
                        content = "Not Found",
                        status = HttpStatusCode.NotFound,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            }
        }
        
        httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
        
        service = UrlShortenerServiceImpl(httpClient, baseUrl)
    }
    
    @Test
    fun `shortenUrl should return correct response`() = runBlocking {
        // Given
        val request = ShortenUrlRequest("https://example.com")
        
        // When
        val response = service.shortenUrl(request)
        
        // Then
        assertEquals("abc123", response.alias)
        assertEquals("https://example.com", response._links.self)
        assertEquals("https://sou.nu/abc123", response._links.short)
    }
    
    @Test
    fun `getOriginalUrl should return correct response`() = runBlocking {
        // Given
        val alias = "abc123"
        
        // When
        val response = service.getOriginalUrl(alias)
        
        // Then
        assertEquals("https://example.com", response.url)
    }
    
    @Test(expected = ClientRequestException::class)
    fun `getOriginalUrl should throw exception when alias not found`() = runBlocking {
        // Given
        val nonExistentAlias = "notfound"
        
        // When - This should throw ClientRequestException
        service.getOriginalUrl(nonExistentAlias)
        
        // Then - The test will pass if an exception is thrown
    }
}
