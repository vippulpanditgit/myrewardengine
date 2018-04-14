package com.myreward.parser.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileProcessingUtil {
	public static String readFile(String fileName) throws IOException {
		List<String> list = new ArrayList<>();

		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			list = stream
					.collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		return list.stream().map(e -> e.toString()).reduce("", String::concat);
	}
	public static void writeFile(String fileName, String[] data) throws IOException {
		Files.write(Paths.get(fileName), (Iterable<String>)Arrays.stream(data)::iterator);
	}
}
