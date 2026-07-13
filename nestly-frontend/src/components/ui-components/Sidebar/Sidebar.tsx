import styles from './Sidebar.module.scss'
import type { SidebarProps } from './Sidebar.types'

const Sidebar = ({ properties }:SidebarProps) => {
  return (
    <div className={styles.Sidebar}>
        <div>
            {properties.map((property)=>{
                return <div>{property}</div>
            })}
        </div>
    </div>
  )
}

export default Sidebar