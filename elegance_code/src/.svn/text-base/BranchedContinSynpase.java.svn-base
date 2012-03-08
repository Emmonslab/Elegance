
public class BranchedContinSynpase 
{
    String continName= "";
    String type = "";
    public BranchedContinSynpase(String newContinName, String newType)
    {
        continName = newContinName;
        type = newType;
    }
    public boolean equals(Object obj)
    {
        if(obj instanceof BranchedContinSynpase)
        {
            BranchedContinSynpase brsyn = (BranchedContinSynpase) obj;
            if(this.type.compareToIgnoreCase(brsyn.type) == 0) return true;
            return false;
        }
        return false;
    }
    public String toString()
    {
        return "" + continName + " " + type;
    }
}