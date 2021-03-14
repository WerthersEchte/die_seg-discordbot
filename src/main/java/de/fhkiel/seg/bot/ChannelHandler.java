package de.fhkiel.seg.bot;

import static de.fhkiel.seg.util.LoggerStore.logger;

import de.fhkiel.seg.Configuration;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.channel.Channel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import reactor.core.publisher.Mono;

/**
 * A class to hold all known channels used by the bot and convenience functions to work with these.
 */
public class ChannelHandler {

  private final Bot bot;
  private Map<String, List<Snowflake>> channels = new HashMap<>();

  /**
   * Instantiates a new Channel handler.
   *
   * @param bot the {@link Bot} using this handler
   */
  public ChannelHandler(Bot bot) {
    this.bot = bot;

    channels.put(Configuration.actionChannelName(), new ArrayList<>());
    channels.put(Configuration.infoChannelName(), new ArrayList<>());
    channels.put(Configuration.logChannelName(), new ArrayList<>());

    findChannels();
  }

  private void findChannels() {
    logger().debug("Searching for channels");
    bot.getClient().getGuilds().toStream()
        .forEach(guild -> guild
            .getChannels()
            .toStream()
            .filter(channel -> channel.getType() == Channel.Type.GUILD_TEXT)
            .filter(channel -> getChannels()
                .containsKey(channel.getName()))
            .forEach(channel -> getChannels()
                .get(channel.getName())
                .add(channel.getId())
            ));
  }

  /**
   * Gets the known auction channel {@link Snowflake}s.
   *
   * @return a {@link Snowflake}-stream of all known auction channel
   */
  public Stream<Snowflake> getAuctionChannel() {
    return channels.get(Configuration.actionChannelName()).stream();
  }

  /**
   * Gets the known info channel {@link Snowflake}s.
   *
   * @return a {@link Snowflake}-stream of all known info channel
   */
  public Stream<Snowflake> getInfoChannel() {
    return channels.get(Configuration.infoChannelName()).stream();
  }

  /**
   * Gets the known log channel {@link Snowflake}s.
   *
   * @return a {@link Snowflake}-stream of all known log channel
   */
  public Stream<Snowflake> getLogChannel() {
    return channels.get(Configuration.logChannelName()).stream();
  }

  /**
   * Gets a specific channel by {@link Snowflake}.
   *
   * @param snowflake the snowflake-id of the wanted channel
   * @return the channel with the corresponding snowflake-id encapsulated in a {@link Mono}
   */
  public Mono<Channel> getChannelBySnowflake(Snowflake snowflake) {
    return bot.getClient().getChannelById(snowflake);
  }

  /**
   * Gets the underlying map containing the known channel {@link Snowflake}s sorted by key.
   * Keys are their name.
   *
   * @return the channels
   */
  public Map<String, List<Snowflake>> getChannels() {
    return channels;
  }

  /**
   * Gets all channel-{@link Snowflake}s in a list.
   *
   * @return a list of {@link Snowflake}s
   */
  public List<Snowflake> getAllChannels() {
    return channels.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
  }
}
