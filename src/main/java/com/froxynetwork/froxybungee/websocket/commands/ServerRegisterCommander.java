package com.froxynetwork.froxybungee.websocket.commands;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.froxynetwork.froxybungee.Froxy;
import com.froxynetwork.froxynetwork.network.websocket.IWebSocketCommander;

/**
 * FroxyBungee Copyright (C) 2019 FroxyNetwork
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author 0ddlyoko
 */
public class ServerRegisterCommander implements IWebSocketCommander {
	private final Logger LOG = LoggerFactory.getLogger(getClass());

	private Pattern spacePattern;

	public ServerRegisterCommander() {
		spacePattern = Pattern.compile(" ");
	}

	@Override
	public String name() {
		return "WebSocketRegister";
	}

	@Override
	public String description() {
		return "Register a new Server";
	}

	@Override
	public void onReceive(String message) {
		// message = <serverId | host | port | motd>
		String[] split = spacePattern.split(message);
		if (split.length < 4) {
			// Error
			LOG.error("Invalid message: {}", message);
			return;
		}
		String serverId = split[0];
		String host = split[1];
		String strPort = split[2];
		int port = -1;
		// 3 = 3 spaces
		String motd = message.substring(split[0].length() + split[1].length() + split[2].length() + 3);
		try {
			port = Integer.parseInt(strPort);
		} catch (NumberFormatException ex) {
			LOG.error("Cannot parse port {}", strPort);
		}

		// All seams ok
		Froxy.getServerManager().registerServer(serverId, host, port, motd);
	}
}
