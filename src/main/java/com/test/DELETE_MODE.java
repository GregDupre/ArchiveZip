package com.test;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;

enum DELETE_MODE {

	ONLY_FILES {
		@Override
		FileVisitResult postVisitDirectory(Path folder, IOException error) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			Files.delete(file);
			return FileVisitResult.CONTINUE;
		}
	},
	ALL {
		@Override
		FileVisitResult postVisitDirectory(Path folder, IOException error) throws IOException {
			if (error != null) {
				throw error;
			}
			Files.delete(folder);
			return FileVisitResult.CONTINUE;
		}

		@Override
		FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			Files.delete(file);
			return FileVisitResult.CONTINUE;
		}
	},
	DISABLED {
		@Override
		FileVisitResult postVisitDirectory(Path folder, IOException error) {
			return FileVisitResult.CONTINUE;
		}

		@Override
		FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			return FileVisitResult.CONTINUE;
		}
	};
	abstract FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException;

	abstract FileVisitResult postVisitDirectory(Path folder, IOException error) throws IOException;

	SimpleFileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			return DELETE_MODE.this.visitFile(file, attrs);
		}

		@Override
		public FileVisitResult postVisitDirectory(Path folder, IOException error) throws IOException {
			return DELETE_MODE.this.postVisitDirectory(folder, error);
		}
	};

	void process(Collection<Path> paths) throws IOException {
		if (this == DELETE_MODE.DISABLED) {
			return;
		}
		for (Path path : paths) {
			Files.walkFileTree(path, visitor);
		}
	}
}