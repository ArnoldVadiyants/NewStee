package com.newstee;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ProfileListFragment extends ListFragment
{


	private static final String ARG_IS_CANAL = "is_canal";

	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 */

	public ProfileListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}




	private static final List<Item> items = new ArrayList<Item>();

	private static class Item
	{
		public final Bitmap icon;
		public final String title;
		public final boolean isCanal;



		public Item(Bitmap icon, String title, boolean isCanal) {
			this.isCanal = isCanal;
			this.icon = icon;
			this.title = title;

		}
	}
	
	public static class ViewHolder 
	{
		public final ImageView imageView;
		public final TextView title;
		public final ImageButton addBtn;

		public ViewHolder(ImageView imageView, TextView title, ImageButton addBtn) {
			this.imageView = imageView;
			this.title = title;
			this.addBtn = addBtn;
		}
	}
	
	private class ProfileListAdapter extends ArrayAdapter<Item>
	{

		public ProfileListAdapter(Context context)
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
				ImageButton imageButton =(ImageButton)view.findViewById(R.id.catalog_item_add_ImageButton);



				view.setTag(new ViewHolder(imageView, titleTextView, imageButton));
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

				if(item.isCanal)
				{
					holder.imageView.setVisibility(View.VISIBLE);
					holder.title.setTypeface(Typeface.DEFAULT);

				}
				else
				{
					holder.imageView.setVisibility(View.GONE);
					holder.title.setTypeface(Typeface.DEFAULT_BOLD);
				//	holder
				}

				holder.title.setText(item.title);
				holder.addBtn.setImageResource(R.drawable.ic_is_added);
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
			items.add(new Item(bm, "Новости Болграда", false));
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
		ListAdapter adapter = new ProfileListAdapter(getActivity());
		setListAdapter(adapter);
	}
}
