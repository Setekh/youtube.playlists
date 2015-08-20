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

import java.io.*;
import java.util.List;

/**
 * @author Vlad Ravenholm on 8/20/2015.
 *
 * Once in a while we do things, to make us happy
 */
public class PlaylistWriter {

	public static void write(List<YoutubePlaylistItem> items, File out) {
		try (FileOutputStream fos = new FileOutputStream(out)) {
			write(fos, "[playlist]");
			write(fos, "NumberOfEntries="+items.size());

			for (int i = 0; i < items.size(); i++) {
				YoutubePlaylistItem item = items.get(i);
				write(fos, "Title"+i+"="+item.name);
				write(fos, "File"+i+"="+"https://youtube.com/watch?v="+item.videoId);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void write(FileOutputStream fos, String value) throws Exception {
		fos.write(value.getBytes("UTF-8"));
		fos.write('\n');
	}
}
