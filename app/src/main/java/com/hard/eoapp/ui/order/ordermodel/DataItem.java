package com.hard.eoapp.ui.order.ordermodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem implements Parcelable {

	@SerializedName("waktu_acara")
	private String waktuAcara;

	@SerializedName("metode_pembayaran")
	private String metodePembayaran;

	@SerializedName("foto")
	private String foto;

	@SerializedName("harga")
	private String harga;

	@SerializedName("nama_pake")
	private String namaPake;

	@SerializedName("tipe paket")
	private String tipePaket;

	@SerializedName("status_order")
	private String statusOrder;

	@SerializedName("item_paket")
	private List<ItemPaketItem> itemPaket;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("paket_id")
	private String paketId;

	@SerializedName("waktu_order")
	private String waktuOrder;

	public DataItem(Parcel in) {
		waktuAcara = in.readString();
		metodePembayaran = in.readString();
		foto = in.readString();
		harga = in.readString();
		namaPake = in.readString();
		tipePaket = in.readString();
		statusOrder = in.readString();
		orderId = in.readString();
		paketId = in.readString();
		waktuOrder = in.readString();
	}

	public static final Creator<DataItem> CREATOR = new Creator<DataItem>() {
		@Override
		public DataItem createFromParcel(Parcel in) {
			return new DataItem(in);
		}

		@Override
		public DataItem[] newArray(int size) {
			return new DataItem[size];
		}
	};

	public DataItem() {

	}

	public void setWaktuAcara(String waktuAcara){
		this.waktuAcara = waktuAcara;
	}

	public String getWaktuAcara(){
		return waktuAcara;
	}

	public void setMetodePembayaran(String metodePembayaran){
		this.metodePembayaran = metodePembayaran;
	}

	public String getMetodePembayaran(){
		return metodePembayaran;
	}

	public void setFoto(String foto){
		this.foto = foto;
	}

	public String getFoto(){
		return foto;
	}

	public void setHarga(String harga){
		this.harga = harga;
	}

	public String getHarga(){
		return harga;
	}

	public void setNamaPake(String namaPake){
		this.namaPake = namaPake;
	}

	public String getNamaPake(){
		return namaPake;
	}

	public void setTipePaket(String tipePaket){
		this.tipePaket = tipePaket;
	}

	public String getTipePaket(){
		return tipePaket;
	}

	public void setStatusOrder(String statusOrder){
		this.statusOrder = statusOrder;
	}

	public String getStatusOrder(){
		return statusOrder;
	}

	public void setItemPaket(List<ItemPaketItem> itemPaket){
		this.itemPaket = itemPaket;
	}

	public List<ItemPaketItem> getItemPaket(){
		return itemPaket;
	}

	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return orderId;
	}

	public void setPaketId(String paketId){
		this.paketId = paketId;
	}

	public String getPaketId(){
		return paketId;
	}

	public void setWaktuOrder(String waktuOrder){
		this.waktuOrder = waktuOrder;
	}

	public String getWaktuOrder(){
		return waktuOrder;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(waktuAcara);
		parcel.writeString(metodePembayaran);
		parcel.writeString(foto);
		parcel.writeString(harga);
		parcel.writeString(namaPake);
		parcel.writeString(tipePaket);
		parcel.writeString(statusOrder);
		parcel.writeString(orderId);
		parcel.writeString(paketId);
		parcel.writeString(waktuOrder);
	}
}