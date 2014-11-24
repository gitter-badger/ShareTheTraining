package controllers.image;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

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
			
			byte[] result = this.setCropAndScaleAvatarUpload(picture, 600);
			
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
	
	private byte[] setCropAndScaleAvatarUpload(FilePart avatarUpload,
			int finalWidthAndHeight) throws IOException {
		BufferedImage img = ImageIO.read(avatarUpload.getFile());
		int x1 = 0, y1 = 0;
		int x2 = Math.min(img.getHeight(), img.getWidth());
		int y2 = x2;
		int croppedWidth = x2 - x1;
		int croppedHeight = y2 - y1;

		BufferedImage cropped = img.getSubimage(x1, y1, croppedWidth,
				croppedHeight);
		BufferedImage resized = cropped;
		if (croppedWidth != finalWidthAndHeight) {
			resized = this.getScaledInstance(cropped, finalWidthAndHeight,
					finalWidthAndHeight,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		ImageIO.write(resized, "jpg", baos);
		baos.flush();
		byte[] result = baos.toByteArray();
		baos.close();
		return result;
	}

	private static BufferedImage getScaledInstance(BufferedImage img,
			int targetWidth, int targetHeight, Object hint,
			boolean higherQuality) {
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) img;
		int w, h;
		if (higherQuality && img.getWidth() > targetWidth
				&& img.getHeight() > targetHeight) {
			// Use multi-step technique: start with original size, then
			// scale down in multiple passes with drawImage()
			// until the target size is reached
			w = img.getWidth();
			h = img.getHeight();
		} else {
			// Use one-step technique: scale directly from original
			// size to target size with a single drawImage() call
			w = targetWidth;
			h = targetHeight;
		}

		do {
			if (higherQuality && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}

			if (higherQuality && h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}

			BufferedImage tmp = new BufferedImage(w, h, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();

			ret = tmp;
		} while (w != targetWidth || h != targetHeight);

		return ret;
	}

}
