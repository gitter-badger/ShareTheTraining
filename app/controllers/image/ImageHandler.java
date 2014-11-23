package controllers.image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import controllers.routes;
import play.Play;
import play.mvc.Http.MultipartFormData.FilePart;

public class ImageHandler implements IImageHandler{

	@Override
	public boolean processImage(FilePart picture,final String fname) throws IOException {
		if (picture != null) {
			String myUploadPath = Play.application().configuration().getString("myUploadPath");
			String directory = myUploadPath;
			File dir = new File(directory);
			String[] oldFilePath = dir.list(new FilenameFilter() {
			    @Override
			    public boolean accept(File dir, String name) {
			        return name.matches(fname);
			    }
			});
			
			
			ImageResize imageResize = new ImageResize();
			byte[] result = imageResize.setCropAndScaleAvatarUpload(picture, 600);
			
		    String fileName = picture.getFilename();
		    String contentType = picture.getContentType(); 
		    File file = picture.getFile();
		    
		    //added lines
		    
//		    file.renameTo(new File(myUploadPath, fileName));
		    FileOutputStream fos = new FileOutputStream(myUploadPath+fileName);
		    fos.write(result);
		    fos.close();
	
	        return true;
		  } else {
			 return false;
		  }
		
	}

}
