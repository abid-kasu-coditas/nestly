import styles from "./PropertyListing.module.scss";
import AddCircleOutlineOutlinedIcon from "@mui/icons-material/AddCircleOutlineOutlined";
import SearchOutlinedIcon from "@mui/icons-material/SearchOutlined";
import VisibilityOutlinedIcon from "@mui/icons-material/VisibilityOutlined";
import EditOutlinedIcon from "@mui/icons-material/EditOutlined";
import DeleteOutlineOutlinedIcon from "@mui/icons-material/DeleteOutlineOutlined";
import LocationOnOutlinedIcon from "@mui/icons-material/LocationOnOutlined";
import BedOutlinedIcon from "@mui/icons-material/BedOutlined";
import CurrencyRupeeOutlinedIcon from "@mui/icons-material/CurrencyRupeeOutlined";
import WifiOutlinedIcon from "@mui/icons-material/WifiOutlined";
import LocalParkingOutlinedIcon from "@mui/icons-material/LocalParkingOutlined";
import { useAppSelector } from "../../redux/store/hooks";



const PropertyListing = () => {

  const userRole = useAppSelector(state => state.authUser.user?.role)
  return (
    <div className={styles.PropertyListing}>
      <div className={styles.PropertyListingHeader}>
        <div>
          <h2>Properties</h2>
          <p>Manage your property listings</p>
        </div>

        {userRole==="OWNER" ? <button className={styles.CreateBtn}>
          <AddCircleOutlineOutlinedIcon />
          New Listing
        </button> : ""}
      </div>

      <div className={styles.Filters}>
        <div className={styles.SearchBar}>
          <SearchOutlinedIcon />
          <input
            type="text"
            placeholder="Search properties..."
          />
        </div>

        <select>
          <option>All Status</option>
          <option>Pending Verification</option>
          <option>Changes Requested</option>
          <option>Approved</option>
          <option>Unpublished</option>
        </select>
      </div>

      <div className={styles.PropertyListingContainer}>
        {[1, 2, 3].map((item) => (
          <div key={item} className={styles.PropertyCard}>
            <img
              src="https://images.unsplash.com/photo-1560185007-c5ca9d2c014d"
              alt="property"
            />

            <div className={styles.CardContent}>
              <div className={styles.CardTop}>
                <h3>2BHK Apartment</h3>

                <span className={styles.Pending}>
                  Pending Verification
                </span>
              </div>

              <div className={styles.Location}>
                <LocationOnOutlinedIcon />
                <span>Pune, Maharashtra</span>
              </div>

              <div className={styles.Price}>
                <CurrencyRupeeOutlinedIcon />
                <span>18,000 / month</span>
              </div>

              <div className={styles.Details}>
                <span>
                  <BedOutlinedIcon />
                  2 Rooms
                </span>

                <span>
                  <WifiOutlinedIcon />
                  WiFi
                </span>

                <span>
                  <LocalParkingOutlinedIcon />
                  Parking
                </span>
              </div>

              <div className={styles.Actions}>
                <button>
                  <VisibilityOutlinedIcon />
                  View
                </button>

                <button>
                  <EditOutlinedIcon />
                  Edit
                </button>

                <button className={styles.DeleteBtn}>
                  <DeleteOutlineOutlinedIcon />
                  Delete
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default PropertyListing;