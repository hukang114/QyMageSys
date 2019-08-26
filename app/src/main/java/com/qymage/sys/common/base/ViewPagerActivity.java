package com.qymage.sys.common.base;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qymage.sys.R;
import com.qymage.sys.common.util.HackyViewPager;


import java.util.List;

import uk.co.senab.photoview.PhotoView;


/**
 * 查看大图
 */
public class ViewPagerActivity extends Activity {

	private ViewPager mViewPager;
	private TextView mTvCount,tv_all;
	public List<String> piclist;
	private int position;
	Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pager);
		mTvCount = (TextView) findViewById(R.id.tv_count);
		tv_all= (TextView) findViewById(R.id.tv_all);
		mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
//		setContentView(mViewPager);	//布局更改后，不再是ViewPager,加上这句会出错
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		intent=getIntent();
		try {
			position=intent.getIntExtra("position",0);
			mTvCount.setText(position + 1+"");
		}catch (Exception e){
		}

		piclist= (List<String>) intent.getSerializableExtra("piclist");
		tv_all.setText("/"+piclist.size());
		if (piclist!=null){
			mViewPager.setAdapter(new SamplePagerAdapter(piclist));
			mViewPager.setCurrentItem(position);
			mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {
					mTvCount.setText(arg0 + 1+"");
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}
				@Override
				public void onPageScrollStateChanged(int arg0) {
				}
			});
		}else{
			return;
		}


	}

	class SamplePagerAdapter extends PagerAdapter {


		public List<String> piclist;

		public SamplePagerAdapter(List<String> piclist) {
			this.piclist = piclist;
		}

//		private static final int[] sDrawables = { R.mipmap.uoko_guide_background_1,
//				R.mipmap.uoko_guide_background_3, R.mipmap.uoko_guide_background_2};

		@Override
		public int getCount() {
			return piclist.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
//			photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			Glide.with(ViewPagerActivity.this).load(piclist.get(position)).into(photoView);
//			photoView.setImageResource(sDrawables[position]);
			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

}
