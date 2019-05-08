//package com.zwl.arcore.myapplication.lib;
//
//import android.content.Context;
//import android.graphics.Point;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.util.Log;
//import com.google.android.gms.maps.model.TileOverlay;
//import com.google.android.gms.maps.model.TileProvider;
//
//import java.util.Map;
//import java.util.concurrent.RejectedExecutionException;
//
//import static android.support.v4.os.LocaleListCompat.create;
//
//public class GoogleMapLayer extends TileProvider {
//    private static  final int MAXLRU_SIZE=1024*1024*8*1;//假设为1M；
//    private final int MIN_LEVEL = 0;//设置缩放的最小级别
//    private final int MAX_LEVEL = 19;//设置缩放的最大级别,这里最大层级19，可以自定义（需要在下方的 MAP_SCALE 和 RESOLUTIONS 中设置相对应的数据）
//    private int GoogleMapLayerType;
//    private MapCacheHelper cacheHelper;
//    private double[] MAP_SCALE = new double[]{591657527.591555,
//            295828763.79577702, 147914381.89788899, 73957190.948944002,
//            36978595.474472001, 18489297.737236001, 9244648.8686180003,
//            4622324.4343090001, 2311162.217155, 1155581.108577, 577790.554289,
//            288895.277144, 144447.638572, 72223.819286, 36111.909643,
//            18055.954822, 9027.9774109999998, 4513.9887049999998, 2256.994353,
//            1128.4971760000001};
//    private double[] RESOLUTIONS = new double[]{156543.03392800014,
//            78271.516963999937, 39135.758482000092, 19567.879240999919,
//            9783.9396204999593, 4891.9698102499797, 2445.9849051249898,
//            1222.9924525624949, 611.49622628138, 305.748113140558,
//            152.874056570411, 76.4370282850732, 38.2185141425366,
//            19.1092570712683, 9.55462853563415, 4.7773142679493699,
//            2.3886571339746849, 1.1943285668550503, 0.59716428355981721,
//            0.29858214164761665};
//    private Point ORIGIN_PNT = new Point(-20037508.342787, 20037508.342787);
//    private int DPI = 96;
//    private int TILEWIDTH = 256;
//    private int TILEHEIGHT = 256;
//    public GoogleMapLayer(int layerType) {
//        super(true);
//        this.GoogleMapLayerType = layerType;
//        this.init();
//    }
//    private void init() {
//        try {
//            getServiceExecutor().submit(() -> GoogleMapLayer.this.initLayer());
//            // 这里是设置缓存到本地的路径 可以自定义
//            String imgPtPath = AppConsts.CACHE_DIR + "/t" + GoogleMapLayerType + "/";
//            cacheHelper=new MapCacheHelper(GoogleMapLayerType,MAXLRU_SIZE,imgPtPath);
//        } catch (RejectedExecutionException rejectedexecutionexception) {
//            Log.e("Google Map Layer", "initialization of the layer failed.", rejectedexecutionexception);
//        }
//    }
//    @Override
//    protected byte[] getTile(int level, int col, int row) throws Exception {
//        byte[] res;
//        if (level > MAX_LEVEL || level < MIN_LEVEL) {
//            return new byte[0];
//        }
//        String s = "Galileo".substring(0, ((3 * col + row) % 8));
//        String url = getMapUrl(level,col,row,s);
//        //开始获取三级缓存1、内存
//        if((res=cacheHelper.getMapCache(url))!=null){
//            return res;
//        }else if((res=cacheHelper.getLocalMapCache(level,col,row))!=null){
//            cacheHelper.setMapCache(url,res);
//            return res;
//        }else{
//            if (isNetworkAvailable(MyApp.getContext())) {
//                Map<String, String> map = null;
//                res= com.esri.core.internal.io.handler.a.a(url, map);
//                cacheHelper.setLocalMapCache(level,col,row,res);
//                cacheHelper.setMapCache(url,res);
//                return res;
//            }else{
//
//            }
//        }
//        return new byte[0];
//    }
//    protected SpatialReference getSptialReference() {
//        return this.getDefaultSpatialReference();
//    }
//    @Override
//    protected void initLayer() {
//        if (getID() == 0L) {
//            nativeHandle = create();
//            changeStatus(com.esri.android.map.event.OnStatusChangedListener.STATUS.fromInt(-1000));
//        } else {
//            this.setDefaultSpatialReference(SpatialReference.create(102113));
//            this.setFullExtent(new Envelope(-20037508.34, -20037508.34, 20037508.34, 20037508.34));
//            this.setTileInfo(new TileInfo(ORIGIN_PNT, MAP_SCALE, RESOLUTIONS, MAP_SCALE.length, DPI, TILEWIDTH, TILEHEIGHT));
//            super.initLayer();
//        }
//    }
//    private  boolean isNetworkAvailable(Context context) {
//        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        if (manager == null) {
//            return false;
//        }
//        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
//        if (networkinfo == null || !networkinfo.isAvailable()) {
//            return false;
//        }
//        return true;
//
//    }
//    public String getMapUrl(int level, int col, int row,String s){
//        String url="";
//        switch (GoogleMapLayerType) {
//            case GoogleMapLayerTypes.IMAGE_GOOGLE_MAP:
//                url = "http://mt" + (col % 4) + ".google.cn/vt/lyrs=s&hl=zh-CN&gl=cn&" +
//                        "x=" + col + "&" +
//                        "y=" + row + "&" +
//                        "z=" + level + "&" +
//                        "s=" + s;
//                break;
//            case GoogleMapLayerTypes.VECTOR_GOOGLE_MAP:
//                url = "http://mt2.google.cn/vt/lyrs=m@158000000&hl=zh-CN&gl=cn&" +
//                        "x=" + col + "&" +
//                        "y=" + row + "&" +
//                        "z=" + level + "&" +
//                        "s=" + s;
//                break;
//            case GoogleMapLayerTypes.TERRAIN_GOOGLE_MAP:
//                url = "http://mt" + (col % 4) + ".google.cn/vt/lyrs=t@131,r@227000000&hl=zh-CN&gl=cn&" +
//                        "x=" + col + "&" +
//                        "y=" + row + "&" +
//                        "z=" + level + "&" +
//                        "s=" + s;
//                break;
//            case GoogleMapLayerTypes.ANNOTATION_GOOGLE_MAP:
//                url = "http://mt" + (col % 4) + ".google.cn/vt/imgtp=png32&lyrs=h@169000000&hl=zh-CN&gl=cn&" +
//                        "x=" + col + "&" +
//                        "y=" + row + "&" +
//                        "z=" + level + "&" +
//                        "s=" + s;
//                break;
//        }
//        return url;
//    }
//
//}