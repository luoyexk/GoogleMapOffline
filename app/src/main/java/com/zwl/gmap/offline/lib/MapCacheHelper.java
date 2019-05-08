package com.zwl.gmap.offline.lib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import java.io.*;

public class MapCacheHelper {

    private int GoogleMapLayerType;
    LruCache<String,byte[]> myLruCache;
    String filePath;

    /**
     *
     * @param googleMapLayerType  地图类型
     * @param mapLRUCapacity   LRUCache的大小
     * @param filePath 存放本地缓存的路径
     */
    public MapCacheHelper(int googleMapLayerType, int mapLRUCapacity, String filePath){
        this.GoogleMapLayerType=googleMapLayerType;
        this.filePath=filePath;
        createDirs(filePath);
        myLruCache=new LruCache<String,byte[]>(mapLRUCapacity){
            @Override
            protected int sizeOf(String key, byte[] value) {
                return value.length;
            }
        };
    }

    private void createDirs(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public byte[] getMapCache(String url){
        return myLruCache.get(url);
    }
    public void setMapCache(String url,byte[] data){
        myLruCache.put(url,data);
    }
    private byte[] getLocalMapCache(String imgPath,String imgName){
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath + imgName);
        if(bitmap==null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    public byte[] getLocalMapCache(int level, int col, int row){
        String imgPath = filePath+ "l" + level + "/";
        String imgName = "c" + col + "r" + row + ".png";
        return getLocalMapCache(imgPath,imgName);
    }
    private void saveFile(String filePath, byte[] data, String fileName) throws IOException {
        Bitmap mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap
        File dirFile = new File(filePath);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        // 检测是否有 .nomedia 文件，该文件防止相册等媒体软件扫描离线地图图片，以免造成不必要的麻烦
        File nomediaFile = new File(filePath + ".nomedia");
        if (!nomediaFile.exists()) {
            nomediaFile.createNewFile();
        }
        File myCaptureFile = new File(filePath + fileName);
        // 判断离线的图片是否已经存在，已经存在的地图不用再次下载（节省流量）
        if (myCaptureFile.exists()) {
            return;
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        mBitmap.compress(Bitmap.CompressFormat.PNG, 80, bos);
        bos.flush();
        bos.close();
    }
    public void setLocalMapCache(int level, int col, int row, byte[] data){
        try {
            String imgPath = filePath+ "l" + level + "/";
            String imgName = "c" + col + "r" + row + ".png";
            saveFile(imgPath,data, imgName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
