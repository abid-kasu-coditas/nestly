import { Outlet } from 'react-router-dom'
import styles from './DashboardLayout.module.scss'
import Sidebar from '../../components/ui-components/Sidebar/Sidebar'

const DashboardLayout = () => {
  return (
    <div className={styles.DashboardLayout}>
        <Sidebar/>
        <Outlet />
    </div>
  )
}

export default DashboardLayout