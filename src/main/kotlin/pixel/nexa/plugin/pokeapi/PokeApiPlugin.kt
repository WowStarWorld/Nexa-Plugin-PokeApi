package pixel.nexa.plugin.pokeapi

import com.triceracode.pokeapi.imp.PokeAPIServiceImp
import okhttp3.OkHttpClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pixel.auxframework.component.annotation.Component
import pixel.auxframework.util.useAuxConfig
import pixel.nexa.core.NexaCore
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URI

@Component
class PokeApiPlugin(nexaCore: NexaCore) {

    class Config {
        var proxy: String? = null
    }

    val config = nexaCore.getDirectory("config/plugins/pokeapi").useAuxConfig<Config>("config.yml")
    val api = PokeAPIServiceImp()
    val log: Logger = LoggerFactory.getLogger(this::class.java)

    fun createHttpClient(): OkHttpClient.Builder = OkHttpClient.Builder()
        .apply {
            if (config.proxy != null) proxy(
                URI.create(config.proxy!!).let { proxyUri ->
                    Proxy(
                        Proxy.Type.entries.first { it.name.lowercase() == proxyUri.scheme },
                        InetSocketAddress(proxyUri.host, proxyUri.port)
                    )
                }
            )
        }

}
