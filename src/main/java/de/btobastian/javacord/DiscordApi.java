/*
 * Copyright (C) 2017 Bastian Oppermann
 *
 * This file is part of Javacord.
 *
 * Javacord is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser general Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * Javacord is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.btobastian.javacord;

import com.neovisionaries.ws.client.WebSocketAdapter;
import de.btobastian.javacord.entities.Game;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.channels.*;
import de.btobastian.javacord.utils.ThreadPool;
import de.btobastian.javacord.utils.ratelimits.RatelimitManager;

import java.util.Collection;
import java.util.Optional;

/**
 * This class is the most important class for your bot, containing all important methods, like registering listener.
 */
public interface DiscordApi {

    /**
     * Gets the used token.
     * The returned token already includes the {@link AccountType#getTokenPrefix()}, so you can use it directly in the
     * authentication header for custom REST calls.
     *
     * @return The used token.
     */
    String getToken();

    /**
     * Gets the thread pool which is internally used.
     *
     * @return The internally used thread pool.
     */
    ThreadPool getThreadPool();

    /**
     * Gets the ratelimit manager for this bot.
     *
     * @return The ratelimit manager for this bot.
     */
    RatelimitManager getRatelimitManager();

    /**
     * Gets the websocket adapter which is used to connect to Discord.
     *
     * @return The websocket adapter.
     */
    WebSocketAdapter getWebSocketAdapter();

    /**
     * Updates the game of this bot, represented as "Playing Half-Life 3" for example.
     *
     * @param name The name of the game.
     */
    void updateGame(String name);

    /**
     * Updates the game of this bot with a streaming url, represented as "Streaming Half-Life 3" for example.
     * The update might not be visible immediately as it's through the websocket and only a limited amount of
     * game status changes is allowed per minute.
     *
     * @param name The name of the game.
     * @param streamingUrl The streaming url of the game.
     */
    void updateGame(String name, String streamingUrl);

    /**
     * Gets the game which should be displayed.
     * This might not be the game which is really displayed in the client, but it's the game which Javacord is trying
     * to set for your bot, so it might change in the client a few seconds afterwards. If you want the game which
     * is currently displayed, get the user object for your bot and get the game from this object.
     *
     * @return The game which should be displayed.
     */
    Optional<Game> getGame();

    /**
     * Disconnects the bot.
     * After disconnecting you should NOT use this instance again.
     */
    void disconnect();

    /**
     * Sets the maximum reconnect attempts in a given time before the bot stops reconnecting.
     * By default the bot stops reconnecting, if the connection failed more than 5 times in the last 5 minutes.
     * It's not recommended to change these values!
     *
     * @param attempts The amount of attempts. Default: 5.
     * @param seconds The time, in which the attempts can happen in seconds. Default: 300.
     */
    void setReconnectRatelimit(int attempts, int seconds);

    /**
     * Gets a server by it's id.
     *
     * @param id The id of the server.
     * @return The server with the given id.
     */
    Optional<Server> getServerById(long id);

    /**
     * Gets a server by it's id.
     *
     * @param id The id of the server.
     * @return The server with the given id.
     */
    default Optional<Server> getServerById(String id) {
        try {
            return getServerById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a collection with all servers the bot is in.
     *
     * @return A collection with all servers the bot is in.
     */
    Collection<Server> getServers();

    /**
     * Gets a channel by it's id.
     *
     * @param id The id of the channel.
     * @return The channel with the given id.
     */
    default Optional<Channel> getChannelById(long id) {
        return getServers().stream()
                .filter(server -> server.getChannelById(id).isPresent())
                .map(server -> (Channel) server.getChannelById(id).orElse(null))
                .findAny();
    }

    /**
     * Gets a channel by it's id.
     *
     * @param id The id of the channel.
     * @return The channel with the given id.
     */
    default Optional<Channel> getChannelById(String id) {
        try {
            return getChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a text channel by it's id.
     *
     * @param id The id of the text channel.
     * @return The text channel with the given id.
     */
    default Optional<TextChannel> getTextChannelById(long id) {
        return getChannelById(id)
                .filter(channel -> channel instanceof TextChannel)
                .map(channel -> (TextChannel) channel);
    }

    /**
     * Gets a text channel by it's id.
     *
     * @param id The id of the text channel.
     * @return The text channel with the given id.
     */
    default Optional<TextChannel> getTextChannelById(String id) {
        try {
            return getTextChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a voice channel by it's id.
     *
     * @param id The id of the voice channel.
     * @return The voice channel with the given id.
     */
    default Optional<VoiceChannel> getVoiceChannelById(long id) {
        return getChannelById(id)
                .filter(channel -> channel instanceof VoiceChannel)
                .map(channel -> (VoiceChannel) channel);
    }

    /**
     * Gets a voice channel by it's id.
     *
     * @param id The id of the voice channel.
     * @return The voice channel with the given id.
     */
    default Optional<VoiceChannel> getVoiceChannelById(String id) {
        try {
            return getVoiceChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a server text channel by it's id.
     *
     * @param id The id of the server text channel.
     * @return The server text channel with the given id.
     */
    default Optional<ServerTextChannel> getServerTextChannelById(long id) {
        return getChannelById(id)
                .filter(channel -> channel instanceof ServerTextChannel)
                .map(channel -> (ServerTextChannel) channel);
    }

    /**
     * Gets a server text channel by it's id.
     *
     * @param id The id of the server text channel.
     * @return The server text channel with the given id.
     */
    default Optional<ServerTextChannel> getServerTextChannelById(String id) {
        try {
            return getServerTextChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a server voice channel by it's id.
     *
     * @param id The id of the server voice channel.
     * @return The server voice channel with the given id.
     */
    default Optional<ServerVoiceChannel> getServerVoiceChannelById(long id) {
        return getChannelById(id)
                .filter(channel -> channel instanceof ServerVoiceChannel)
                .map(channel -> (ServerVoiceChannel) channel);
    }

    /**
     * Gets a server voice channel by it's id.
     *
     * @param id The id of the server voice channel.
     * @return The server voice channel with the given id.
     */
    default Optional<ServerVoiceChannel> getServerVoiceChannelById(String id) {
        try {
            return getServerVoiceChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a private channel by it's id.
     *
     * @param id The id of the private channel.
     * @return The private channel with the given id.
     */
    default Optional<PrivateChannel> getPrivateChannelById(long id) {
        return getChannelById(id)
                .filter(channel -> channel instanceof PrivateChannel)
                .map(channel -> (PrivateChannel) channel);
    }

    /**
     * Gets a private channel by it's id.
     *
     * @param id The id of the private channel.
     * @return The private channel with the given id.
     */
    default Optional<PrivateChannel> getPrivateChannelById(String id) {
        try {
            return getPrivateChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a group channel by it's id.
     *
     * @param id The id of the group channel.
     * @return The group channel with the given id.
     */
    default Optional<GroupChannel> getGroupChannelById(long id) {
        return getChannelById(id)
                .filter(channel -> channel instanceof GroupChannel)
                .map(channel -> (GroupChannel) channel);
    }

    /**
     * Gets a group channel by it's id.
     *
     * @param id The id of the group channel.
     * @return The group channel with the given id.
     */
    default Optional<GroupChannel> getGroupChannelById(String id) {
        try {
            return getGroupChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

}
