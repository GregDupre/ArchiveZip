package com.test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Main {

	public static void main(String args[]) throws Exception {
		List<String> directories = Arrays.asList("D:\\CRE", "D:\\SWIFT");
		String zipPath = "D://result2.zip";
		String zipName = "";
		//
		Optional<Path> archive = ZipUtil.createArchive(zipPath, zipName, directories, DELETE_MODE.DISABLED);
		ZipUtil.log("Archive " + archive.orElseThrow(Exception::new).toAbsolutePath());
	}

}
