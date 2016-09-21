package com.xxl.search.example.core.model;

import java.util.List;

/**
 * 商户信息
 */
public class ShopDTO {

	private int shopid;				// 商户ID		(唯一ID)
	private String shopname;		// 商户名		(测试中文分词, 模糊搜索)
	private int cityid;				// 城市ID		(测试单标签搜索)
	private List<Integer> taglist;	// 标签列表		(测试多标签搜索)
	private int score;				// 业务分数		(测试默认排序)
	private int hotscore;			// 热门分数		(测试热门排序)

	public ShopDTO(int shopid, String shopname, int cityid, List<Integer> taglist, int score, int hotscore) {
		this.shopid = shopid;
		this.shopname = shopname;
		this.cityid = cityid;
		this.taglist = taglist;
		this.score = score;
		this.hotscore = hotscore;
	}

	public int getShopid() {
		return shopid;
	}

	public void setShopid(int shopid) {
		this.shopid = shopid;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public int getCityid() {
		return cityid;
	}

	public void setCityid(int cityid) {
		this.cityid = cityid;
	}

	public List<Integer> getTaglist() {
		return taglist;
	}

	public void setTaglist(List<Integer> taglist) {
		this.taglist = taglist;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getHotscore() {
		return hotscore;
	}

	public void setHotscore(int hotscore) {
		this.hotscore = hotscore;
	}


	/**
	 * 商户索引字段
     */
	public static class ShopParam {
		public static final String SHOP_ID = "shopid";
		public static final String SHOP_NAME = "shopname";
		public static final String CITY_ID = "cityid";
		public static final String TAG_ID = "taglist";
		public static final String SCORE = "score";
		public static final String HOT_SCORE = "hotscore";
	}

	/**
	 * 城市字典
	 */
	public enum CityEnum{
		BEIJING(1, "北京"),
		SHNGHAI(2, "上海"),
		SHENZHEN(3, "深圳");

		public final int cityid;
		public final String cityname;
		CityEnum(int cityid, String cityname) {
			this.cityid = cityid;
			this.cityname = cityname;
		}
		public int getCityid() {
			return cityid;
		}
		public String getCityname() {
			return cityname;
		}

		public static CityEnum match(int cityid){
			for (CityEnum city: CityEnum.values()) {
				if (city.cityid==cityid) {
					return city;
				}
			}
			return null;
		}
	}

	/**
	 * 标签字典
	 */
	public enum TagEnum{
		TAG01(1, "地铁"),
		TAG02(2, "西式"),
		TAG03(3, "中式"),
		TAG04(4, "草坪");
		public final int tagid;
		public final String tagname;
		TagEnum(int tagid, String tagname) {
			this.tagid = tagid;
			this.tagname = tagname;
		}
		public int getTagid() {
			return tagid;
		}
		public String getTagname() {
			return tagname;
		}

		public static TagEnum match(int tagid){
			for (TagEnum tag: TagEnum.values()) {
				if (tag.tagid==tagid) {
					return tag;
				}
			}
			return null;
		}
	}

}
