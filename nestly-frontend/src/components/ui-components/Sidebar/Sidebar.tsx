import { useNavigate } from 'react-router-dom'
import { Sidebar_items } from '../../../constants/constants'
import { useAppSelector } from '../../../redux/store/hooks'
import styles from './Sidebar.module.scss'
import { ViewSidebarRounded } from '@mui/icons-material';
import type { SidebarProps } from './Sidebar.types';

const Sidebar = ({ closeSidebarFn }:SidebarProps) => {

  // const userRole= useAppSelector(state => state.authUser.user?.role)
  const userRole = "Admin"; 
  const sidebarItems = Sidebar_items[(userRole ?? "") as keyof typeof Sidebar_items]
  console.log(userRole)
  console.log(sidebarItems)
  const navigate = useNavigate()

  return (
    <div className={styles.Sidebar}>
        <div  className={styles.SidebarCloseBtnContainer}>
          <span onClick={()=>{
            closeSidebarFn()
          }}>
            <ViewSidebarRounded className={styles.SidebarCloseBtn}/>
          </span>
        </div>
        <div className={styles.SidebarOptionsContainer}>
            {sidebarItems.map((item)=>{
              return <div onClick={()=>{
                navigate(item.link )
              }} className={styles.SidebarOptions}>{item.name}</div>
            })}
        </div>
    </div>
  )
}

export default Sidebar