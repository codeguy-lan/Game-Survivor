package lan.survivor.adapter;

import java.util.List;
import lan.survivor.R;
import lan.survivor.element.item.ItemElement;
import lan.survivor.utils.DataBaseLogic;
import lan.survivor.utils.RenderResPool;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

	private Context pContext;
	private List<ItemElement> pItems;

	static class ViewHolder {
		public ImageView image;
		public TextView text;
	}

	public GridAdapter(Context c, List<ItemElement> list) {
		pContext = c;
		pItems = list;
	}

	public void updateList(List<ItemElement> list) {
		pItems = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return pItems.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View gridItemView = convertView;
		if (convertView == null) {
			gridItemView = View.inflate(pContext, R.layout.grid_list, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.image = (ImageView) gridItemView.findViewById(R.id.grid_item_icon);
			viewHolder.text = (TextView) gridItemView.findViewById(R.id.grid_item_name);
			gridItemView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) gridItemView.getTag();
		holder.image.setScaleType(ImageView.ScaleType.CENTER);
		holder.image.setImageBitmap(RenderResPool.getImg(pItems.get(position).speciesID));
		holder.text.setText(DataBaseLogic.getTitleAndDescription(pItems.get(position).speciesID)[0]);

		return gridItemView;
	}

}
