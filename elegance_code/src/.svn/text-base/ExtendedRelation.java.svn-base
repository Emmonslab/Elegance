
public class ExtendedRelation 
{
    ExtendedCellObject obj1;
    ExtendedCellObject obj2;
    String type;
    public ExtendedRelation(ExtendedCellObject newObj1, ExtendedCellObject newObj2, String newType)
    {
        obj1 = newObj1;
        obj2 = newObj2;
        type = newType;
    }
    public ExtendedRelation()
    {
        obj1 = null;
        obj2 = null;
        type = null;
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof ExtendedRelation)
        {
            ExtendedRelation exrel = (ExtendedRelation) obj;
            if(this.obj1.compareTo(exrel.obj1) == 0 && this.obj2.compareTo(exrel.obj2) == 0)
            {
                return true;
            }
            else if(this.obj1.compareTo(exrel.obj2) == 0 && this.obj2.compareTo(exrel.obj1) == 0)
            {
                return true;
            }
            return false;
        }
        else return this.equals(obj);
    }
    public String toString()
    {
        return "" + obj1 + "\n" + obj2 + "\n" + type;
    }
}