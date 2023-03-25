package com.matt;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * Currently unimplemented.  When resources such as save files and
 * images are incorporated, this will handle all the IO work of
 * finding and reading the files.
 *
 * I thought the imagery of a spider crawling around the files to
 * retrieve info for the program was fun.
 *
 * @author Matthew Williams
 *
 */
public class FileSpider {

	public FileSpider() {}

	public ArrayList<String> getFileText(String path) {
		ArrayList<String> endList = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(new File(path));
			while (scanner.hasNextLine()) {
				endList.add(scanner.nextLine());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("[FileSpider] Cannot find file at " + path);
		}
		return endList;
	}

	public BufferedImage getImage(String path) {
		try {
			return ImageIO.read(FileSpider.class.getResource(path));
		} catch (IOException e) {
			return null;
		}
	}
}
