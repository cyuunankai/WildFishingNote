package com.simple.wildfishingnote.animation.hover;

import java.util.ArrayList;
import java.util.List;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.simple.wildfishingnote.bean.animation.hover.ImageIntentBean;
import com.simple.wildfishingnote.bean.animation.hover.ImageSrcIntent;

public class HoverSwitch {

	private ImageView rootImage = null;
	private ObjectAnimator rootImageExpandRotationAnimator = null;
	private ObjectAnimator rootImageShrinkRotationAnimator = null;

	private List<ImageIntentBean> itemBtnImageList = new ArrayList<ImageIntentBean>();
	private List<ImageIntentBean> itemTextImageList = new ArrayList<ImageIntentBean>();

	private List<ObjectAnimator> itemExpandAnimatorList = new ArrayList<ObjectAnimator>();
	private List<ObjectAnimator> itemShrinkAnimatorList = new ArrayList<ObjectAnimator>();

	private boolean isItemShow = false;
	
	private final static int BOTTOM_MARGIN = 10;
	private final static int ITEM_BUTTON_RIGHT_MARGIN = 10;
	private final static int ITEM_TEXT_RIGHT_MARGIN = 70;
	private final static float ROTATION = 215f;
	private final static float TRANSLATION_DISTANCE = 80f;
	
	private final static int ROOT_EXPAND_ROTATION_DURATION = 200;
	private final static int ITEM_EXPAND_DURATION = 300;
	private final static int ROOT_SHRINK_ROTATION_DURATION = 200;
	private final static int ITEM_SHRINK_DURATION = 100;

	public void init(ViewGroup parent, int rootImageSrc, List<ImageSrcIntent> imageSrcIntentList, Context context) {
		addImage(parent, rootImageSrc, imageSrcIntentList, context);
		hideItems();
		addAnimator();
		registeRootImageClickListener();
	}

	/**
	 * add image to parent view
	 */
	private void addImage(ViewGroup parent, int rootImageSrc, List<ImageSrcIntent> imageSrcIntentList, final Context context) {
		
		// root image
		rootImage = createImage(rootImageSrc, context);
		setImageBtnPosition(rootImage, ITEM_BUTTON_RIGHT_MARGIN);

		for (ImageSrcIntent imageSrcIntent : imageSrcIntentList) {
			int imageBtnSrc = imageSrcIntent.getItemButtonSrc();
			int imageTextSrc = imageSrcIntent.getItemTextSrc();
			Class intentClass = imageSrcIntent.getIntentClass();
			
			// item button
			ImageView itemBtnImage = createImage(imageBtnSrc, context);
			setImageBtnPosition(itemBtnImage, ITEM_BUTTON_RIGHT_MARGIN);

			parent.addView(itemBtnImage);
			
			ImageIntentBean imageIntent = new ImageIntentBean();
			imageIntent.setImageView(itemBtnImage);
			imageIntent.setIntentClass(intentClass);
			itemBtnImageList.add(imageIntent);
			
			
			// item text
			ImageView itemTextImage = createImage(imageTextSrc, context);
			setImageTextPosition(itemTextImage, ITEM_TEXT_RIGHT_MARGIN);

			parent.addView(itemTextImage);
			
			imageIntent = new ImageIntentBean();
			imageIntent.setImageView(itemTextImage);
			imageIntent.setIntentClass(intentClass);
			itemTextImageList.add(imageIntent);
		}
		
		// top z-index
		parent.addView(rootImage);
	}

	/**
	 * add animator for image
	 */
	private void addAnimator() {
		// root image animator
		setRootImageExpandRotationAnimation(rootImage);
		setRootImageShrinkRotationAnimation(rootImage);

		// item button animator
		float distanceY = -TRANSLATION_DISTANCE;
		for (ImageIntentBean imageIntentBean : itemBtnImageList) {
			ImageView imageView = imageIntentBean.getImageView();
			// expand
			ObjectAnimator animator = getItemImageBtnExpandAnimation(imageView, distanceY);
			itemExpandAnimatorList.add(animator);
			animator = getItemImageBtnShrinkAnimation(imageView, distanceY);
			// shrink
			itemShrinkAnimatorList.add(animator);
			distanceY = distanceY - TRANSLATION_DISTANCE;
		}
		
		// item text animator
		distanceY = -TRANSLATION_DISTANCE;
		for (ImageIntentBean imageIntentBean : itemTextImageList) {
			ImageView imageView = imageIntentBean.getImageView();
			// expand
			ObjectAnimator animator = getItemImageTextBtnExpandAnimation(imageView, distanceY);
			itemExpandAnimatorList.add(animator);
			// shrink
			animator = getItemImageTextShrinkAnimation(imageView, distanceY);
			itemShrinkAnimatorList.add(animator);
			distanceY = distanceY - TRANSLATION_DISTANCE;
		}

	}

	/**
	 * registe root image click listener
	 */
	private void registeRootImageClickListener() {
		rootImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				execRootImageClick(isItemShow);
			}
		});
	}
	
	/**
	 * exec root image click
	 */
	public void execRootImageClick(boolean isItemShowParam){
		if (isItemShowParam) {
			// item is shown
			isItemShow = false;
			hideItems();
			
			// root image shrink rotation animate
			rootImageShrinkRotationAnimator.start();
			
			// item image shrink animate
			for (ObjectAnimator animator : itemShrinkAnimatorList) {
				animator.start();
			}
		} else {
			// item is not shown
			isItemShow = true;
			showItems();
			
			// root image expand rotation animate
			rootImageExpandRotationAnimator.start();
			
			// item image expand animate
			for (ObjectAnimator animator : itemExpandAnimatorList) {
				animator.start();
			}
		}
	}

	/**
	 * create image
	 */
	private ImageView createImage(int src, Context context) {
		ImageView imageView = new ImageView(context);
		imageView.setImageResource(src);

		return imageView;
	}

	/**
	 * set root image and item image position 
	 */
	private void setImageBtnPosition(ImageView imageView, int rightMargin) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		params.bottomMargin = BOTTOM_MARGIN;
		params.rightMargin = rightMargin;
		imageView.setLayoutParams(params);
	}

	/**
	 * set item text position
	 */
	private void setImageTextPosition(ImageView imageView, int rightMargin) {
		setImageBtnPosition(imageView, rightMargin);
	}

	/**
	 * set root image expand rotation animation
	 */
	private void setRootImageExpandRotationAnimation(ImageView rootImageView) {
		PropertyValuesHolder holderR = PropertyValuesHolder.ofFloat("rotation", 0f, ROTATION);
		rootImageExpandRotationAnimator = ObjectAnimator.ofPropertyValuesHolder(rootImageView, holderR);
		rootImageExpandRotationAnimator.setDuration(ROOT_SHRINK_ROTATION_DURATION);
		rootImageExpandRotationAnimator.setInterpolator(new OvershootInterpolator());

	}
	
	/**
	 * get item button expand animation
	 */
	private ObjectAnimator getItemImageBtnExpandAnimation(ImageView imageView, float y) {
		PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("translationY", 0f, y);
		final ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(imageView, holderY);
		animator.setDuration(ITEM_EXPAND_DURATION);
		animator.setInterpolator(new OvershootInterpolator());

		return animator;
	}
	
	/**
	 * get item text expand animation
	 */
	private ObjectAnimator getItemImageTextBtnExpandAnimation(ImageView imageView, float y) {
		return getItemImageBtnExpandAnimation(imageView, y);
	}

	/**
	 * set root image shrink rotation animation
	 */
	private void setRootImageShrinkRotationAnimation(ImageView rootImageView) {
		PropertyValuesHolder holderR = PropertyValuesHolder.ofFloat("rotation", ROTATION, 0f);
		rootImageShrinkRotationAnimator = ObjectAnimator.ofPropertyValuesHolder(rootImageView, holderR);
		rootImageShrinkRotationAnimator.setDuration(ROOT_EXPAND_ROTATION_DURATION);
		rootImageShrinkRotationAnimator.setInterpolator(new OvershootInterpolator());
	}

	/**
	 * get item button shrink animation
	 */
	private ObjectAnimator getItemImageBtnShrinkAnimation(ImageView imageView, float y) {
		PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("translationY", y, 0f);
		final ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(imageView, holderY);
		animator.setDuration(ITEM_SHRINK_DURATION);
		animator.setInterpolator(new LinearInterpolator());

		return animator;
	}

	/**
	 * get item text shrink animation
	 */
	private ObjectAnimator getItemImageTextShrinkAnimation(ImageView imageView, float y) {
		return getItemImageBtnShrinkAnimation(imageView, y);
	}

	/**
	 * hide item
	 */
	private void hideItems() {
		
		for (ImageIntentBean imageIntentBean : itemBtnImageList) {
			ImageView imageView = imageIntentBean.getImageView();
			imageView.setVisibility(View.INVISIBLE);
		}
		
		for (ImageIntentBean imageIntentBean : itemTextImageList) {
			ImageView imageView = imageIntentBean.getImageView();
			imageView.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * show item
	 */
	private void showItems() {
		for (ImageIntentBean imageIntentBean : itemBtnImageList) {
			ImageView imageView = imageIntentBean.getImageView();
			imageView.setVisibility(View.VISIBLE);
		}
		
		for (ImageIntentBean imageIntentBean : itemTextImageList) {
			ImageView imageView = imageIntentBean.getImageView();
			imageView.setVisibility(View.VISIBLE);
		}
	}
	
	public List<ImageIntentBean> getItemBtnImageList() {
		return itemBtnImageList;
	}

	public void setItemBtnImageList(List<ImageIntentBean> itemBtnImageList) {
		this.itemBtnImageList = itemBtnImageList;
	}

	public List<ImageIntentBean> getItemTextImageList() {
		return itemTextImageList;
	}

	public void setItemTextImageList(List<ImageIntentBean> itemTextImageList) {
		this.itemTextImageList = itemTextImageList;
	}


}
