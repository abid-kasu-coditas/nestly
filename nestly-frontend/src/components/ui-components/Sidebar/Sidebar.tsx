import { useNavigate } from 'react-router-dom'
import { Sidebar_items } from '../../../constants/constants'
import { useAppSelector } from '../../../redux/store/hooks'
import styles from './Sidebar.module.scss'

const Sidebar = () => {

  const userRole= useAppSelector(state => state.authUser.user?.role)
  const sidebarItems = Sidebar_items[(userRole ?? "") as keyof typeof Sidebar_items]
  console.log(userRole)
  console.log(sidebarItems)
  const navigate = useNavigate()

  return (
    <div className={styles.Sidebar}>
        <div className={styles.SidebarOptionsContainer}>
            {sidebarItems.map((item)=>{
              console.log(item.name)
              return <div onClick={()=>{
                navigate(item.link )
              }} className={styles.SidebarOptions}>{item.name}</div>
            })}
        </div>
    </div>
  )
}

export default Sidebar