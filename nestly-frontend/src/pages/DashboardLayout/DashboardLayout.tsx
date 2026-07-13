import { Outlet } from 'react-router-dom'
import styles from './DashboardLayout.module.scss'

const DashboardLayout = () => {
  return (
    <div className={styles.DashboardLayout}>
        <div></div>
        <Outlet />
    </div>
  )
}

export default DashboardLayout