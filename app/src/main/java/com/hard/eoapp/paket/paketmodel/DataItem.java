package com.hard.eoapp.paket.paketmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem implements Parcelable {

	@SerializedName("status_paket")
	private String statusPaket;

	@SerializedName("foto")
	private String foto;

	@SerializedName("harga")
	private String harga;

	@SerializedName("nama_pake")
	private String namaPake;

	@SerializedName("tipe paket")
	private String tipePaket;

	@SerializedName("item_paket")
	private List<ItemPaketItem> itemPaket;

	@SerializedName("paket_id")
	private String paketId;

	protected DataItem(Parcel in) {
		statusPaket = in.readString();
		foto = in.readString();
		harga = in.readString();
		namaPake = in.readString();
		tipePaket = in.readString();
		itemPaket = in.createTypedArrayList(ItemPaketItem.CREATOR);
		paketId = in.readString();
	}

	public DataItem(){

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

	public void setStatusPaket(String statusPaket){
		this.statusPaket = statusPaket;
	}

	public String getStatusPaket(){
		return statusPaket;
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

	public void setItemPaket(List<ItemPaketItem> itemPaket){
		this.itemPaket = itemPaket;
	}

	public List<ItemPaketItem> getItemPaket(){
		return itemPaket;
	}

	public void setPaketId(String paketId){
		this.paketId = paketId;
	}

	public String getPaketId(){
		return paketId;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(statusPaket);
		parcel.writeString(foto);
		parcel.writeString(harga);
		parcel.writeString(namaPake);
		parcel.writeString(tipePaket);
		parcel.writeTypedList(itemPaket);
		parcel.writeString(paketId);
	}
}