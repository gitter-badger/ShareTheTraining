package controllers.image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import controllers.routes;
import controllers.user.UserHandler;
import play.Play;
import play.mvc.Http.MultipartFormData.FilePart;

public class ImageHandler implements IImageHandler{

	@Override
	public String processImage(FilePart picture, String oldpath) throws IOException {
		if (picture != null) {
			
			if(oldpath!=null){
				File f = new File(oldpath);
				if(f.exists()){
					f.delete();
				}
			}
			
			ImageResize imageResize = new ImageResize();
			byte[] result = imageResize.setCropAndScaleAvatarUpload(picture, 600);
			
		    String fileName = picture.getFilename();
		    String contentType = picture.getContentType(); 
		    File file = picture.getFile();
		    
		    //added lines
		    String myUploadPath = Play.application().configuration().getString("myUploadPath");
//		    file.renameTo(new File(myUploadPath, fileName));
		    String imagePath = myUploadPath+fileName;
		    FileOutputStream fos = new FileOutputStream(imagePath);
		    fos.write(result);
		    fos.close();
		    
	        return imagePath;
		  } else {
			 return null;
		  }
		
	}

}
