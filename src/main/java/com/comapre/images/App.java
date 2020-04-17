package com.comapre.images;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;

public class App {
	
	public static BufferedImage getDifferenceImage(BufferedImage img1, BufferedImage img2) {
	    // convert images to pixel arrays...
	    final int w = img1.getWidth(),
	            h = img1.getHeight(), 
	            highlight = Color.MAGENTA.getRGB();
	    final int[] p1 = img1.getRGB(0, 0, w, h, null, 0, w);
	    final int[] p2 = img2.getRGB(0, 0, w, h, null, 0, w);
	    // compare img1 to img2, pixel by pixel. If different, highlight img1's pixel...
	    for (int i = 0; i < p1.length; i++) {
	        if (p1[i] != p2[i]) {
	            p1[i] = highlight;
	        }
	    }
	    // save img1's pixels to a new BufferedImage, and return it...
	    // (May require TYPE_INT_ARGB)
	    final BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    out.setRGB(0, 0, w, h, p1, 0, w);
	    return out;
	}
	

	public static void main(String[] args) {
		String url1 = "https://www.hoteltonight-test.com/";
		String url2 = "https://feature-2.hoteltonight-test.com/";
		
		
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+File.separator+"drivers"+File.separator+"chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get(url1);
		
		driver.manage().window().maximize();
		Shutterbug.shootPage(driver,ScrollStrategy.WHOLE_PAGE,500,true).withName("image1").save();
		driver.quit();
		
		driver = new ChromeDriver();
		driver.get(url2);
		
		driver.manage().window().maximize();
		Shutterbug.shootPage(driver,ScrollStrategy.WHOLE_PAGE,500,true).withName("image2").save();
		driver.quit();
	
		try {
			ImageIO.write(
			        getDifferenceImage(
			                ImageIO.read(new File(System.getProperty("user.dir")+File.separator+"screenshots"+File.separator+"image1.png")),
			                ImageIO.read(new File(System.getProperty("user.dir")+File.separator+"screenshots"+File.separator+"image2.png"))),
			        "png",
			        new File(System.getProperty("user.dir")+File.separator+"screenshots"+File.separator+"output.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

