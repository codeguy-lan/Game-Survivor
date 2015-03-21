package lan.survivor.adapter;

import java.util.List;
import lan.survivor.R;
import lan.survivor.application.GameApplication;
import lan.survivor.element.item.ItemElement;
import lan.survivor.element.item.tool.Bottle;
import lan.survivor.utils.DataBaseLogic;
import lan.survivor.utils.RenderResPool;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemListAdapter extends ArrayAdapter<ItemElement> {

	private List<ItemElement> pList;

	static class ViewHolder {
		public ImageView image;
		public TextView text;
		public Button dropButton;
	}

	public ItemListAdapter(Context context, int textViewResourceId, List<ItemElement> list) {
		super(context, textViewResourceId, list);
		pList = list;
	}

	public void updateList(List<ItemElement> list) {
		pList = list;
		// notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			rowView = View.inflate(GameApplication.getAppContext(), R.layout.item_list, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.image = (ImageView) rowView.findViewById(R.id.item_icon);
			viewHolder.text = (TextView) rowView.findViewById(R.id.item_name);
			viewHolder.dropButton = (Button) rowView.findViewById(R.id.item_drop);
			viewHolder.dropButton.setTag(position);
			rowView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) rowView.getTag();
		ItemElement ie = pList.get(position);
		holder.image.setImageBitmap(RenderResPool.getImg(ie.speciesID));
		if (ie.getMaxStack() == 1) {
			if (ie instanceof Bottle) {
				holder.text.setText(DataBaseLogic.getTitleAndDescription(ie.speciesID)[0] + "(" + ((Bottle) ie).fluidAmount + ")");
			} else {
				holder.text.setText(DataBaseLogic.getTitleAndDescription(ie.speciesID)[0]);
			}
		} else {
			// holder.text.setText(GameApplication.getAppContext().getString(RefTable.getNameID(ie.speciesID)).split(",")[1] + "X" + ie.stackCount);
			holder.text.setText(DataBaseLogic.getTitleAndDescription(ie.speciesID)[0] + "X" + ie.stackCount);
		}
		// holder.dropButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// int pos = (Integer) v.getTag();
		// EventManager.drop(GameView.survivor.currentTerrainBlock(),
		// InventoryManager.inventory.get(pos));
		// notifyDataSetChanged();
		// }
		// });
		return rowView;
	}
}
