package com.j2igf.framework.graphics.staticGFX;

import com.j2igf.framework.graphics.Bitmap;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class Image extends Bitmap
{
	public Image(Bitmap bitmap, boolean copy)
	{
		this.originX = bitmap.getOriginX();
		this.originY = bitmap.getOriginY();
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
		if (copy)
			this.pixels = Arrays.copyOf(bitmap.getPixels(), bitmap.getPixels().length);
		else
			this.pixels = bitmap.getPixels();
	}

	public Image(String path)
	{
		if (path == null)
			return;

		BufferedImage image = null;
		try
		{
			image = ImageIO.read(new File(path));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		assert image != null;

		this.originX = 0;
		this.originY = 0;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.pixels = image.getRGB(0, 0, width, height, null, 0, width);
		image.flush();
	}

	public void saveToFile(String path, String name)
	{
		File outputFile = new File(path + "/" + name + ".png");
		try
		{
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			image.setRGB(0, 0, width, height, pixels, 0, width);
			ImageIO.write(image, "png", outputFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
