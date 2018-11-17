package com.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	public static void main(String args[]) throws Exception {
		List<String> directories = Arrays.asList("D:\\CRE", "D:\\SWIFT");
		String zipPath = "D://result.zip";
		String zipName = "";
		//
		Optional<Path> archive = createArchive(zipPath, zipName, directories, DELETE_MODE.DISABLED);

	}

	public static Optional<Path> createArchive(String zipPath, String zipName, Collection<String> directories, DELETE_MODE deletionMode) throws Exception {
		Collection<Path> validDirectories = directories.stream().map(Paths::get).filter(Files::exists).filter(Files::isDirectory).collect(Collectors.toList());
		if (validDirectories.isEmpty()) {
			return Optional.empty();
		}

		Path zip = Paths.get(zipPath, zipName);
		Files.deleteIfExists(zip);

		try (ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(zip))) {
			for (Path root : validDirectories) {
				Consumer<Path> add = addNewEntry(root, out);
				Files.walk(root).filter(Files::isRegularFile).filter(Files::isReadable).forEach(add);
			}
		} catch (Exception e) {
			throw e;
		}

		deletionMode.process(validDirectories);
		return Optional.of(zip);
	}

	private static Consumer<Path> addNewEntry(Path rootPath, ZipOutputStream zip) {
		return newEntry -> {
			try {
				File f = new File(rootPath.getFileName().toString(), rootPath.relativize(newEntry).toString());
				zip.putNextEntry(new ZipEntry(f.getPath()));
				zip.write(Files.readAllBytes(newEntry));
				zip.closeEntry();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		};
	}
}