/**
 * promisCRM.Struts Apr 21, 2010
 */
package org.iita.crm.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author mobreza
 * 
 */
public class TagCloud {
	private List<TagCloudEntity> tags = new ArrayList<TagCloudEntity>();
	private long totalCount = 0;

	public void add(String tag, long count) {
		TagCloudEntity tc = new TagCloudEntity();
		this.totalCount += count;
		tc.count = count;
		tc.tag = tag;
		this.tags.add(tc);
	}

	/**
	 * @return the totalCount
	 */
	public long getTotalCount() {
		return this.totalCount;
	}

	/**
	 * @return the tags
	 */
	public List<TagCloudEntity> getTags() {
		return this.tags;
	}

	public class TagCloudEntity {
		private String tag;
		private long count;
		private int relevance;

		/**
		 * @return the tag
		 */
		public String getTag() {
			return this.tag;
		}

		/**
		 * @param tag the tag to set
		 */
		public void setTag(String tag) {
			this.tag = tag;
		}

		/**
		 * @return the count
		 */
		public long getCount() {
			return this.count;
		}

		/**
		 * @param count the count to set
		 */
		public void setCount(long count) {
			this.count = count;
		}

		/**
		 * @param totalCount
		 * @param size
		 */
		public void relevance(long totalCount, int size) {
			if (totalCount == 0 || size == 0)
				return;
			double avg = (double) totalCount / (double) size;
			double rate = (double) this.count / avg;
			if (rate > 10) {
				relevance = 5;
			} else if (rate > 3) {
				relevance = 4;
			} else if (rate > 1) {
				relevance = 3;
			} else if (rate > 0.4) {
				relevance = 2;
			} else {
				relevance = 1;
			}
		}

		/**
		 * @return the relevance
		 */
		public int getRelevance() {
			return this.relevance;
		}
	}

	/**
	 * Method to be called to do final calculation bits
	 * 
	 * @return
	 */
	public TagCloud prepare() {
		Collections.sort(this.tags, new Comparator<TagCloudEntity>() {
			public int compare(TagCloudEntity o1, TagCloudEntity o2) {
				return o1.tag.compareTo(o2.tag);
			}
		});
		int size = this.tags.size();
		for (TagCloudEntity tc : this.tags) {
			tc.relevance(this.totalCount, size);
		}
		return this;
	}
}