package controllers.image;

import java.io.IOException;

import play.mvc.Http.MultipartFormData.FilePart;

public interface IImageHandler {
	

	public String processImage(FilePart picture, String name) throws IOException;
}
