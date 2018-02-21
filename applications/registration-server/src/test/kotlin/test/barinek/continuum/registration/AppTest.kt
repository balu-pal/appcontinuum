package test.barinek.continuum.registration


import io.barinek.continuum.discovery.DiscoveryController
import io.barinek.continuum.discovery.InstanceDataGateway
import io.barinek.continuum.redissupport.RedisConfig
import io.barinek.continuum.registration.App
import io.barinek.continuum.restsupport.BasicApp
import io.barinek.continuum.restsupport.RestTemplate
import org.apache.http.message.BasicNameValuePair
import org.eclipse.jetty.server.handler.HandlerList
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class AppTest {
    val pool = RedisConfig().getPool("discovery")

    var app: BasicApp = object : BasicApp() {
        override fun getPort() = 8888

        override fun handlerList() = HandlerList().apply {
            addHandler(DiscoveryController(mapper, InstanceDataGateway(pool, 5000)))
        }
    }

    @Before
    fun setUp() {
        pool.resource.flushAll()
        app.start()
    }

    @After
    fun tearDown() {
        app.stop()
    }

    @Test
    fun testApp() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        App().start()

        Thread.sleep(2000)

        assertEquals("[]",
                RestTemplate().get("http://localhost:8081/projects", "application/vnd.appcontinuum.v2+json",
                        BasicNameValuePair("accountId", "1673"))
        )
    }
}