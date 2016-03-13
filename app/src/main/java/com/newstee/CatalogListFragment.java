package com.newstee;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;



public class CatalogListFragment extends ListFragment
{
	private final static String TAG = "CatalogListFragment";

	private static final String ARG_IS_CANAL = "is_canal";
	private boolean mIsCanal;

	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 */
	public static CatalogListFragment newInstance(boolean isCanal) {
		CatalogListFragment fragment = new CatalogListFragment();
		Bundle args = new Bundle();
		args.putBoolean(ARG_IS_CANAL, isCanal);
		fragment.setArguments(args);
		return fragment;
	}

	public CatalogListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mIsCanal = getArguments().getBoolean(ARG_IS_CANAL);
	}




	private static final List<Item> items = new ArrayList<Item>();

	private static class Item
	{
		public final Bitmap icon;
		public final String title;
		public final boolean isAdded;


		public Item(Bitmap icon, String title, boolean isAdded) {
			this.icon = icon;
			this.title = title;
			this.isAdded = isAdded;
		}
	}
	
	public static class ViewHolder 
	{
		public final ImageView imageView;
		public final TextView title;
		public final ImageButton addBtn;
		public final LinearLayout catalogFeed;

		public ViewHolder(ImageView imageView, TextView title, ImageButton addBtn, LinearLayout catalogFeed) {
			this.imageView = imageView;
			this.title = title;
			this.addBtn = addBtn;
			this.catalogFeed = catalogFeed;
		}
	}
	
	private class CatalogListAdapter extends ArrayAdapter<Item>
	{

		public CatalogListAdapter(Context context)
		{
			super(context, R.layout.catalog_item_title, items);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View view = convertView;
			ViewHolder holder = null;
			if(view == null)
			{
				view = LayoutInflater.from(getContext()).inflate(R.layout.catalog_item_title, parent, false);
				TextView titleTextView = (TextView)view.findViewById(R.id.title_TextView);
				ImageView imageView = (ImageView)view.findViewById(R.id.canal_icon_ImageVew);
				LinearLayout catalogFeed = (LinearLayout)view.findViewById(R.id.catalog_feed);
				ImageButton imageButton =(ImageButton)view.findViewById(R.id.catalog_item_add_ImageButton);



				view.setTag(new ViewHolder(imageView, titleTextView, imageButton, catalogFeed));
			}
			if(holder == null && view != null)
			{
				Object tag = view.getTag();
				if(tag instanceof ViewHolder)
				{
					holder = (ViewHolder)tag;
				}
			}
			Item item = getItem(position);
			if(item != null && holder != null)
			{

				if(mIsCanal)
				{
				//	holder.imageView.set
					holder.catalogFeed.setTag(position);
					holder.catalogFeed.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							startActivity(new Intent(getContext(), CanalFragmentActivity.class));

							Log.d(TAG, "List_item onCLick" + " " + (v.getTag()));
						}
					});

				}
				else
				{
					holder.catalogFeed.setOnClickListener(null);
					holder.imageView.setVisibility(View.GONE);
					holder.title.setTypeface(Typeface.DEFAULT_BOLD);
				//	holder
				}
				holder.addBtn.setTag(position);
				holder.addBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						Toast.makeText(getActivity(), "Canal #" + v.getTag() + " is added", Toast.LENGTH_SHORT).show();
						((ImageButton) v).setImageResource(R.drawable.ic_is_added);
					}
				});
				holder.title.setText(item.title);
			//	holder
			}
			return view;
		}
	}
	
	static
	{
		Bitmap bm = Bitmap.createBitmap(100, 100, Bitmap.Config.ALPHA_8);
		for(int i = 0; i<50; i++) {
			items.add(new Item(bm,"Petrovich",true ));
			items.add(new Item(bm, "Нвости Болграда", false));
			items.add(new Item(bm,"Спорт",true ));
			items.add(new Item(bm,"Судьи",false ));
		}

	}
	
	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
{
	return inflater.inflate(R.layout.catalog_listview, container, false);
}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		ListAdapter adapter = new CatalogListAdapter(getActivity());
		setListAdapter(adapter);
	}
}
