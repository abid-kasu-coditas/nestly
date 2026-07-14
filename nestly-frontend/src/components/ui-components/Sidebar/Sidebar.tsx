import { Sidebar_items } from '../../../constants/constants'
import { useAppSelector } from '../../../redux/store/hooks'
import styles from './Sidebar.module.scss'
import type { SidebarProps } from './Sidebar.types'

const Sidebar = ({ properties }:SidebarProps) => {
  type role = "Admin" | "User"
  const userRole: role = useAppSelector(state => state.authUser.user)

  return (
    <div className={styles.Sidebar}>
        <div className={styles.SidebarOptionsContainer}>
            {Sidebar_items}
        </div>
    </div>
  )
}

export default Sidebar