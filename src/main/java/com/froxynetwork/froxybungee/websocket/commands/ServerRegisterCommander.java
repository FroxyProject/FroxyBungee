package com.froxynetwork.froxybungee.websocket.commands;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.froxynetwork.froxybungee.Froxy;
import com.froxynetwork.froxynetwork.network.websocket.IWebSocketCommander;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

/**
 * MIT License
 *
 * Copyright (c) 2019 FroxyNetwork
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * @author 0ddlyoko
 */
public class ServerRegisterCommander implements IWebSocketCommander {
	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Override
	public String name() {
		return "WebSocketRegister";
	}

	@Override
	public String description() {
		return "Register a new Server";
	}

	@Override
	public From from() {
		return From.WEBSOCKET;
	}

	@Override
	public void onReceive(String from, String message) {
		// message = <serverId | host | port | motd>
		String[] split = message.split(" ");
		if (split.length != 4) {
			// Error
			LOG.error("Invalid message: {}", message);
			return;
		}
		String serverId = split[0];
		String host = split[1];
		String strPort = split[2];
		int port = -1;
		String motd = split[3];
		try {
			port = Integer.parseInt(strPort);
		} catch (NumberFormatException ex) {
			LOG.error("Cannot parse port {}", strPort);
		}

		// All seams ok
		LOG.info("Registering a new server ! (id = {}, host = {}, port = {}, motd = {})", serverId, host, port, motd);
		ServerInfo info = ProxyServer.getInstance().constructServerInfo(serverId,
				InetSocketAddress.createUnresolved(host, port), motd, false);
		Froxy.bungee().getServers().put(serverId, info);
	}
}
