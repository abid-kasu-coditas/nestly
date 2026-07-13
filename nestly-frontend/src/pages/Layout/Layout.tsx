import { Outlet } from 'react-router-dom'
import Header from '../../components/ui-components/Header/Header'
import styles from './Layout.module.scss'

const Layout = () => {
  return (
    <div className={styles.Layout}>
        <Header/>
        <Outlet/>
    </div>
  )
}

export default Layout;