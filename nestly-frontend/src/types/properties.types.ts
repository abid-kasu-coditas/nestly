type STATUS = "PENDING" | "DONE"

export interface PropertyType {
    title : string;
    description  : string;
    location : string;
    rent_amount : number;
    amenities : string;
    status : STATUS
}

