package lan.survivor.window;

import lan.survivor.R;
import lan.survivor.element.BaseElement;
import lan.survivor.element.item.ItemElement;
import lan.survivor.utils.DataBaseLogic;
import lan.survivor.utils.Misc.CreationItemAction;
import lan.survivor.utils.RenderResPool;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CreationResultWindow {

	private Context pContext;
	private View pCreateResultView;
	private TextView pTextName, pTextDetail;
	private ImageView pIcon;
	private Button pButtonSet, pButtonTake, pButtonCancel;
	private CreationItemAction actionListener;
	private AlertDialog pCreateResultWindow;

	public CreationResultWindow(Context context) {
		pContext = context;
		pCreateResultView = View.inflate(pContext, R.layout.creation_result_view, null);
		pTextName = (TextView) pCreateResultView.findViewById(R.id.cresult_item_name);
		pTextDetail = (TextView) pCreateResultView.findViewById(R.id.cresult_item_detail);
		pIcon = (ImageView) pCreateResultView.findViewById(R.id.cresult_item_icon);
		pIcon.setScaleType(ImageView.ScaleType.CENTER);
		pButtonSet = (Button) pCreateResultView.findViewById(R.id.cresult_btnSet);
		pButtonTake = (Button) pCreateResultView.findViewById(R.id.cresult_btnTake);
		pButtonCancel = (Button) pCreateResultView.findViewById(R.id.cresult_btnCancel);

		pButtonSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pCreateResultWindow.dismiss();
				actionListener.onSet();
			}
		});

		pButtonTake.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pCreateResultWindow.dismiss();
				actionListener.onTake();
			}
		});

		pButtonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pCreateResultWindow.dismiss();
			}
		});
		pCreateResultWindow = new AlertDialog.Builder(pContext).create();
		pCreateResultWindow.setView(pCreateResultView);
	}

	public void setListener(CreationItemAction listener) {
		actionListener = listener;
	}

	public void showCreationResult(BaseElement e) {
		String titleAndDescription[] = DataBaseLogic.getTitleAndDescription(e.speciesID);
		pTextName.setText(titleAndDescription[0]);
		pTextDetail.setText(titleAndDescription[1]);
		pIcon.setImageBitmap(RenderResPool.getImg(e.speciesID));
		if (e instanceof ItemElement) {
			pButtonSet.setVisibility(View.INVISIBLE);
			pButtonTake.setVisibility(View.VISIBLE);
		} else {
			pButtonTake.setVisibility(View.INVISIBLE);
			pButtonSet.setVisibility(View.VISIBLE);
		}
		pCreateResultWindow.show();
	}
}
