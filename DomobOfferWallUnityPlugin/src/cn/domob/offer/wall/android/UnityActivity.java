package cn.domob.offer.wall.android;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import cn.domob.data.OErrorInfo;
import cn.domob.data.OManager;
import cn.domob.data.OManager.AddVideoWallListener;
import cn.domob.data.OManager.ConsumeListener;
import cn.domob.data.OManager.ConsumeStatus;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

public class UnityActivity extends UnityPlayerActivity {

	private OManager mDomobOfferWallManager;


	
	private Context mContext = null;
	private String mDefaultCamera;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mDefaultCamera = "Main Camera";
	}

	public void setCamera(String camera){
		mDefaultCamera = camera;
	}
	public void loadOfferWall(String paramPublisherID, String paramUserID) {
		Log.i("------->>>>>loadOfferWall", "paramPublisherID:"
				+ paramPublisherID + "paramUserID:" + paramUserID);
		mDomobOfferWallManager = new OManager(this,
				paramPublisherID, paramUserID);
		mDomobOfferWallManager
				.setAddWallListener(new OManager.AddWallListener() {

					@Override
					public void onAddWallFailed(
							OErrorInfo mDomobOfferWallErrorInfo) {
						UnityPlayer.UnitySendMessage(mDefaultCamera,
								"onAddWallFailed",
								mDomobOfferWallErrorInfo.toString());
					}

					@Override
					public void onAddWallClose() {
						UnityPlayer.UnitySendMessage(mDefaultCamera,
								"onAddWallClose", "");
					}

					@Override
					public void onAddWallSucess() {
						UnityPlayer.UnitySendMessage(mDefaultCamera,
								"onAddWallSucess", "");
					}
				});
		mDomobOfferWallManager.loadOfferWall();

	}
	
	
	public void cacheVideoAd(String paramPublisherID, String paramUserID) {
		Log.i("------->>>>>loadOfferWall", "paramPublisherID:"
				+ paramPublisherID + "paramUserID:" + paramUserID);

		mDomobOfferWallManager = new OManager(this,
				paramPublisherID, paramUserID);
		mDomobOfferWallManager.cacheVideoAd();
	}

	public void loadVideoOfferWall(String paramPublisherID, String paramUserID) {
		Log.i("------->>>>>loadOfferWall", "paramPublisherID:"
				+ paramPublisherID + "paramUserID:" + paramUserID);

		mDomobOfferWallManager = new OManager(this,
				paramPublisherID, paramUserID);
		mDomobOfferWallManager.setAddVideoWallListener(new AddVideoWallListener() {
			
			@Override
			public void onAddWallSucess() {
				UnityPlayer.UnitySendMessage(mDefaultCamera,
						"onAddViedoWallClose", "");
			}
			
			@Override
			public void onAddWallFailed(OErrorInfo mOErrorInfo) {
				UnityPlayer.UnitySendMessage(mDefaultCamera,
						"onAddViedoWallClose", "");
			}
			
			@Override
			public void onAddWallClose() {
				UnityPlayer.UnitySendMessage(mDefaultCamera,
						"onAddViedoWallClose", "");
			}
		});
		mDomobOfferWallManager.presentVideoWall();

	}

	public void checkPoints(String paramPublisherID, String paramUserID) {
		Log.i("------->>>>>checkPoints", "paramPublisherID:" + paramPublisherID
				+ "paramUserID:" + paramUserID);
		mDomobOfferWallManager = new OManager(this,
				paramPublisherID, paramUserID);
		mDomobOfferWallManager
				.setCheckPointsListener(new OManager.CheckPointsListener() {

					@Override
					public void onCheckPointsSucess(final int point,
							final int consumed) {

						int result_CheckPoints = point - consumed;
						String result = "当前积分:" + result_CheckPoints + "总积分："
								+ point + "总消费积分：" + consumed;
						showToast(result);
						UnityPlayer.UnitySendMessage(mDefaultCamera,
								"onCheckPointsSucess",
								String.valueOf(result_CheckPoints));
					}

					@Override
					public void onCheckPointsFailed(
							OErrorInfo mDomobOfferWallErrorInfo) {
						UnityPlayer.UnitySendMessage(mDefaultCamera,
								"onCheckPointsFailed",
								mDomobOfferWallErrorInfo.toString());
					}


		

				});
		mDomobOfferWallManager.checkPoints();

	}

	public void consumePoints(String paramPublisherID, String paramUserID,
			String paramPoints) {
		Log.i("------->>>>>consumePoints", "paramPublisherID:"
				+ paramPublisherID + "paramUserID:" + paramUserID
				+ "paramPoints:" + paramPoints);
		mDomobOfferWallManager = new OManager(this,
				paramPublisherID, paramUserID);
		mDomobOfferWallManager
				.setConsumeListener(new ConsumeListener() {

					@Override
					public void onConsumeFailed(
							OErrorInfo mDomobOfferWallErrorInfo) {
						showToast(mDomobOfferWallErrorInfo.toString());
						UnityPlayer.UnitySendMessage(mDefaultCamera,
								"onConsumeFailed", String
										.valueOf(mDomobOfferWallErrorInfo
												.toString()));
					}

					@Override
					public void onConsumeSucess(final int point,
							final int consumed, final ConsumeStatus cs) {

						switch (cs) {
						case SUCCEED:
							int result_SUCCEED = point - consumed;
							String result = "消费成功:" + "当前积分:" + result_SUCCEED
									+ "总积分：" + point + "总消费积分：" + consumed;
							showToast(result);
							UnityPlayer.UnitySendMessage(mDefaultCamera,
									"onConsumeSucess",
									String.valueOf(result_SUCCEED));
							break;
						case OUT_OF_POINT:
							String result_OUT_OF_POINT = "总积分不足，消费失败：" + "总积分："
									+ point + "总消费积分：" + consumed;
							showToast(result_OUT_OF_POINT);
							UnityPlayer.UnitySendMessage(mDefaultCamera,
									"onConsumeFailed",
									String.valueOf(result_OUT_OF_POINT));
							break;
						case ORDER_REPEAT:
							String result_ORDER_REPEAT = "订单号重复，消费失败：" + "总积分："
									+ point + "总消费积分：" + consumed;
							showToast(result_ORDER_REPEAT);
							UnityPlayer.UnitySendMessage(mDefaultCamera,
									"onConsumeFailed",
									String.valueOf(result_ORDER_REPEAT));
							break;

						default:
							showToast("未知错误");
							UnityPlayer.UnitySendMessage(mDefaultCamera,
									"onConsumeFailed",
									String.valueOf("An unknown error"));
							break;
						}

					}
				});

		if (paramPoints != null && !paramPoints.equals("")
				&& paramPoints.length() != 0) {
			mDomobOfferWallManager.consumePoints(Integer.parseInt(paramPoints));
		}
	}

	public void showToast(final String content) {

		((Activity) mContext).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Log.i("----->>>result", content + "");
				Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	
	   @Override
	   public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	         if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_LANDSCAPE) {
	              // land donothing is ok
	         } else if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT) {
	              // port donothing is ok
	         }
	   } 
}