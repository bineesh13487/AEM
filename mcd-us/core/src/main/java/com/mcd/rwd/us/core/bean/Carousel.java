package com.mcd.rwd.us.core.bean;

import java.util.List;

/**
 * 
 * @author prahlad.d
 *
 */

public class Carousel {

	private String animation;

    private String speed;

    private String slideshow;
    
    private List<ImageGallery> imagegallery;

	public String getAnimation() {
		return animation;
	}

	public void setAnimation(String animation) {
		this.animation = animation;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getSlideshow() {
		return slideshow;
	}

	public void setSlideshow(String slideshow) {
		this.slideshow = slideshow;
	}

	public List<ImageGallery> getImagegallery() {
		return imagegallery;
	}

	public void setImagegallery(List<ImageGallery> imagegallery) {
		this.imagegallery = imagegallery;
	}
}
