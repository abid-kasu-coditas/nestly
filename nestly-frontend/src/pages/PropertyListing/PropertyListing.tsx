import styles from "./PropertyListing.module.scss"

const PropertyListing = () => {
  return (
    <div className={styles.PropertyListing}>
        <div className={styles.PropertyListingHeader}>search FIlter</div>
        <div className={styles.PropertyListingContainer}>list of properies</div>
    </div>
  )
}

export default PropertyListing