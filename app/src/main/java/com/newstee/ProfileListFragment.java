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
import android.widget.TextView;
import android.widget.Toast;

import com.newstee.model.data.DataPost;
import com.newstee.model.data.Tag;
import com.newstee.model.data.TagLab;
import com.newstee.model.data.UserLab;
import com.newstee.network.FactoryApi;
import com.newstee.network.interfaces.NewsTeeApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileListFragment extends ListFragment
{

	private  List<Item> items = new ArrayList<Item>();
	private static final String ARG_IS_CANAL = "is_canal";
	private ProfileListAdapter adapter;

	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 */
	private void update()
	{
		items.clear();
		List<Tag> tags = UserLab.getInstance().getAddedTags();
		for (Tag t : tags) {
			items.add(new Item(t.getId(),null, t.getNameTag(), false));
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		if(adapter ==null) {
			return;
		}
		update();
		adapter.notifyDataSetChanged();


	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser)
		{
			if(adapter ==null) {
				return;
			}
				update();
				adapter.notifyDataSetChanged();


		}
	}
	public ProfileListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		update();

	}






	private static class Item
	{
		public final String id;
		public final Bitmap icon;
		public final String title;
		public final boolean isCanal;



		public Item(String id, Bitmap icon, String title, boolean isCanal) {
			this.id = id;
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
					holder.addBtn.setVisibility(View.GONE);

				}
				else
				{
					holder.imageView.setVisibility(View.GONE);
					holder.title.setTypeface(Typeface.DEFAULT_BOLD);
					final String id = item.id.trim();
					holder.addBtn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(final View v) {
							NewsTeeApiInterface nApi = FactoryApi.getInstance(getActivity());
							Call<DataPost> call = nApi.addTags(id);
							call.enqueue(new Callback<DataPost>() {
								@Override
								public void onResponse(Call<DataPost> call, Response<DataPost> response) {
									if (response.body().getResult().equals(Constants.RESULT_SUCCESS)) {
										UserLab.getInstance().addTag(TagLab.getInstance().getTag(id));

										if (UserLab.getInstance().isAddedTag(id)) {
											((ImageButton) v).setImageResource(R.drawable.ic_is_added);
										} else {
											((ImageButton) v).setImageResource(R.drawable.news_to_add_button);
										}


									} else {
										Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
									}
								}

								@Override
								public void onFailure(Call<DataPost> call, Throwable t) {
									Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
								}
							});
						}
					});
				//	holder
					if (UserLab.getInstance().isAddedTag(id)) {
						holder.addBtn.setImageResource(R.drawable.ic_is_added);
					}
					else
					{
						holder.addBtn.setImageResource(R.drawable.news_to_add_button);
					}
				}

				holder.title.setText(item.title);


			//	holder
			}
			return view;
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
		adapter = new ProfileListAdapter(getActivity());
		setListAdapter(adapter);

		TextView emptyTextView = (TextView) getListView().getEmptyView();
		emptyTextView.setText(getString(R.string.empty_my_catalog));
	}
}
