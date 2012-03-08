import java.util.LinkedHashSet;
import java.util.Set;

public class Display2DOptions {

	private Set<String> continFilterCustom;
	private ContinFilterType continFilterType = ContinFilterType.custom_number;
	
	private SynapseType synapseType = SynapseType.all;

	private int zoom = 1;

	private String dtype = "Z_Y";

	private LinkedHashSet<Display2DOptions.NeuronPair> neurons2Connect;	
	public static enum ContinFilterType {
		custom_number, custom_name, custom_color_code
	}

	public static enum SynapseType {

		all("All"), pre("Presynapse"), post("Postsynapse"), gap("Gap Junction");

		SynapseType(String label) {
			this.label = label;
		}

		private String label;

		public String getLabel() {
			return this.label;
		}

		public static SynapseType[] getValues() {// TODO:remove
			return values();
		}
		
		public static SynapseType get(String label) {
			
			for(SynapseType t:values()) {
				if (t.label.equals(label)) return t;
			}
			
			return null;
		}
		
		public static int getIndex(SynapseType type) {
			int i=0;
			for(SynapseType t:values()) {
				if (t == type) return i;
				i++;
			}
			
			return 0;
		}
		
	}

	public ContinFilterType getContinFilterType() {
		return continFilterType;
	}

	public void setContinFilterType(ContinFilterType continFilterType) {
		this.continFilterType = continFilterType;
	}

	public Set<String> getContinFilterCustom() {
		return continFilterCustom;
	}

	public void setContinFilterCustom(Set<String> continFilterCustom) {
		this.continFilterCustom = continFilterCustom;
	}

	public SynapseType getSynapseType() {
		return synapseType;
	}

	public void setSynapseType(SynapseType synapseType) {
		this.synapseType = synapseType;
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public String getDtype() {
		return dtype;
	}

	public void setDtype(String dtype) {
		this.dtype = dtype;
	}


	public static class NeuronPair {
		private Integer fromId;
		private Integer toId;
		public NeuronPair(Integer fromId, Integer toId) {
			super();
			this.fromId = fromId;
			this.toId = toId;
		}
		public Integer getFromId() {
			return fromId;
		}
		public Integer getToId() {
			return toId;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fromId == null) ? 0 : fromId.hashCode());
			result = prime * result + ((toId == null) ? 0 : toId.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			NeuronPair other = (NeuronPair) obj;
			if (fromId == null) {
				if (other.fromId != null)
					return false;
			} else if (!fromId.equals(other.fromId))
				return false;
			if (toId == null) {
				if (other.toId != null)
					return false;
			} else if (!toId.equals(other.toId))
				return false;
			return true;
		}
		public void setFromId(Integer fromId) {
			this.fromId = fromId;
		}
		public void setToId(Integer toId) {
			this.toId = toId;
		}
		
	}


	public LinkedHashSet<Display2DOptions.NeuronPair> getNeurons2Connect() {
		return neurons2Connect;
	}

	public void setNeurons2Connect(LinkedHashSet<Display2DOptions.NeuronPair> neurons2Connect) {
		this.neurons2Connect = neurons2Connect;
	}


	
	
	
}