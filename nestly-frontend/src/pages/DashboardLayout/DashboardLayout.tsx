import { Outlet } from 'react-router-dom'
import styles from './DashboardLayout.module.scss'
import Sidebar from '../../components/ui-components/Sidebar/Sidebar'
import { useState } from 'react'
import { ViewSidebarOutlined } from '@mui/icons-material'

const DashboardLayout = () => {

  const [isSidebarVisible, setIsSidebarVisible] = useState(false)

  return (
    <div className={styles.DashboardLayout}>
          <div className={styles.SidebarContainer}>
            {isSidebarVisible ? <Sidebar closeSidebarFn={()=>{
            setIsSidebarVisible(false)
          }}/> : 
            <span onClick={()=>{
              setIsSidebarVisible(true)
            }} className={styles.OpenSidebar}>
              <ViewSidebarOutlined className={styles.OpenSidebarBtn} />
            </span>
          }
          </div>
            <Outlet />
      </div>
  )
}

export default DashboardLayout