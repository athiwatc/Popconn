package org.jinstagram.entity.tags;

import java.util.List;

import org.jinstagram.entity.common.Meta;

import com.google.gson.annotations.SerializedName;

public class TagSearchFeed {
	@SerializedName("meta")
	private Meta meta;

	@SerializedName("data")
	private List<TagInfoData> tagList;

	/**
	 * @return the meta
	 */
	public Meta getMeta() {
		return meta;
	}

	/**
	 * @param meta the meta to set
	 */
	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public List<TagInfoData> getTagList() {
		return tagList;
	}

	public void setTagList(List<TagInfoData> tagList) {
		this.tagList = tagList;
	}

    @Override
    public String toString() {
        return String.format("TagSearchFeed [meta=%s, tagList=%s]", meta, tagList);
    }
}
