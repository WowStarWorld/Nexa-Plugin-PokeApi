package pixel.nexa.plugin.pokeapi

import okhttp3.OkHttpClient
import pixel.auxframework.component.annotation.Component
import pixel.auxframework.core.registry.identifierOf
import pixel.nexa.core.resource.AssetsMap
import pixel.nexa.network.command.Command
import pixel.nexa.network.command.CommandSession
import pixel.nexa.network.command.NexaCommand
import pixel.nexa.network.message.MessageFragments
import pixel.nexa.network.message.MutableMessageData

@Command("pokeapi:pokemon")
@Component
class PokemonCommand(private val plugin: PokeApiPlugin, private val assetsMap: AssetsMap) : NexaCommand() {

    val httpClient: OkHttpClient = plugin.createHttpClient().build()

    @Action
    suspend fun handle(@Argument session: CommandSession, @Option("name") pokemonName: String) {
        session.replyLazy {
            val pokemon = plugin.api.pokemon().byName(pokemonName.lowercase()).execute().body()!!
            val name = pokemon.name.split(" ").filterNot(String::isEmpty).joinToString(" ") { it.replaceFirstChar(Character::toUpperCase) }
            MutableMessageData().add(
                MessageFragments.pageView(
                    assetsMap.getPage(identifierOf("pokeapi:pokemon.html"))
                ) {
                    put("pokemonStats", pokemon.stats.associate { it.stat.name to it.baseStat })
                    put("pokemonName", name)
                    put("frontUrl", pokemon.sprites.frontDefault)
                    put("backUrl", pokemon.sprites.backDefault)
                    put("pokemonType", pokemon.types.joinToString(" ") { it.type.name })
                    put("pokemon", pokemon)
                }
            )
        }
    }

}