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

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Vlad Ravenholm on 8/20/2015.
 */
public class YoutubeApplication extends Application {

	static {
		System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 6.1; rv:40.0) Gecko/20100101 Firefox/40.0");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		AnchorPane root = FXMLLoader.load(getClass().getResource("/main.fxml"));

		TextField link = (TextField) root.lookup("#link");
		ListView list = (ListView) root.lookup("#list");
		Button process = (Button) root.lookup("#process");
		Button save = (Button) root.lookup("#save");

		AtomicReference<String> pageName = new AtomicReference<>();

		process.setOnAction(event -> {
			list.getItems().clear();

			if(link.getText().isEmpty() || !link.getText().contains("youtube")) {
				System.out.println("Invalid thing");
				return;
			}


			try { new URL(link.getText()); } catch (Exception e) { System.out.println("Invalid thing"); return; }

			try {
				if(link.getText().contains("watch?v")) { // ooops, redirect
					String sourceLink = link.getText();
					String playId = "";

					int index = sourceLink.lastIndexOf("&list=") + 1;
					playId = sourceLink.substring(index);
					playId = playId.substring(0, playId.indexOf('&'));

					link.setText("https://www.youtube.com/playlist?"+playId);
				}

				Document document = Jsoup.connect(link.getText()).get();
				System.out.println("At it.");

				Element titleElement = document.select("title").get(0);
				String title = titleElement.text().substring(0, titleElement.text().indexOf("-")).trim();
				pageName.set(title);

				Elements elements = document.select("td.pl-video-title > a.pl-video-title-link");
				elements.forEach(element -> {
					String name = element.text();
					String href = element.attr("href");
					href = href.substring(href.lastIndexOf("?") + 1);

					list.getItems().add(new YoutubePlaylistItem(name, href));
				});
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		});

		save.setOnAction(event -> {
			if (list.getItems().isEmpty())
				return;

			FileChooser chooser = new FileChooser();
			ObservableList<ExtensionFilter> extensionFilters = chooser.getExtensionFilters();
			extensionFilters.add(new ExtensionFilter("Media Playlist(*.pls)", "*.pls"));

			chooser.setInitialDirectory(new File("."));
			chooser.setInitialFileName(pageName+".pls");

			File file = chooser.showSaveDialog(primaryStage);

			if(file != null)
				PlaylistWriter.write(list.getItems(), file);
		});

		primaryStage.setTitle("Youtube playlist parser");
		primaryStage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
		primaryStage.sizeToScene();
		primaryStage.setMinHeight(root.getPrefHeight() * .5);
		primaryStage.setMinWidth(root.getPrefWidth() * .5);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
