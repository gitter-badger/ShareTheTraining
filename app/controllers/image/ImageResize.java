package controllers.image;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import play.mvc.Http.MultipartFormData.FilePart;

public class ImageResize {

	public byte[] setCropAndScaleAvatarUpload(FilePart avatarUpload,
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

	public static BufferedImage getScaledInstance(BufferedImage img,
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
