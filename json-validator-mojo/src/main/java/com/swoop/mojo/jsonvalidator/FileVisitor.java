package com.swoop.mojo.jsonvalidator;

import java.io.File;

abstract class FileVisitor<E extends Exception>
{
	private File base;
	private String extension;

	public FileVisitor(File base, String extension)
	{
		this.base = base;
		this.extension = extension;
	}

	public void traverse()
		throws E
	{
		traverse(base);
	}

	private void traverse(File f)
		throws E
	{
		if (f.isDirectory()) {
			File[] listing = f.listFiles();
			if (listing != null) {
				for (File child : listing) {
					traverse(child);
				}
			}
		}
		else if (f.getName().endsWith(extension)) {
			visit(f);
		}
	}

	abstract protected void visit(File f)
		throws E;
}
