package com.swoop.mojo.jsonvalidator;

import java.io.File;
import java.io.IOException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @goal validate
 * @phase validate
 */
public class JsonValidatorMojo extends AbstractMojo
{
	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	MavenProject project;

	/**
	 * @parameter expression="${basedir}"
	 * @required
	 * @readonly
	 */
	String basedir;

	/**
	 * @parameter default-value="src"
	 */
	String sourceDirectory = "src";

	/**
	 * @parameter default-value=".json"
	 */
	String sourceExtension = ".json";

	@Override
	public void execute()
		throws MojoFailureException
	{
		final JsonValidator validator = new JsonValidator();

		new FileVisitor<MojoFailureException>(new File(new File(basedir), sourceDirectory), sourceExtension) {
			@Override
			protected void visit(File file)
				throws MojoFailureException
			{
				try {
					validator.validate(file);
				}
				catch (IOException e) {
					throw new MojoFailureException("JSON validation failure", e);
				}
			}
		}.traverse();
	}
}
