/*
 * Copyright (C) 2015 Vlad Ravenholm
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package corvus.corax.youtube;

/**
 * @author Vlad Ravenholm on 8/20/2015.
 */
public class YoutubePlaylistItem {
	public final String name;
	public String videoId;
	public String playlistId;
	public int index;

	public YoutubePlaylistItem(String name, String source) {
		this.name = name;

		String[] elems = source.split("&");
		for (int i = 0; i < elems.length; i++) {
			String elem = elems[i];
			String[] keys = elem.split("=");

			switch (keys[0]) {
				case "v": {
					videoId = keys[1];
					break;
				}
				case "list": {
					playlistId = keys[1];
					break;
				}
				case "index": {
					index = Integer.parseInt(keys[1]);
					break;
				}
			}
		}
	}

	@Override
	public String toString() {
		return "Name["+name+"] Index["+index+"] VideoId["+videoId+"]";
	}
}
